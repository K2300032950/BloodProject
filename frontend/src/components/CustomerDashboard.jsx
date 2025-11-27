import React, { useState, useEffect } from "react";
import { FaUserCircle, FaSignOutAlt } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./CustomerDashboard.css";

const API_URL = "http://localhost:30025/api/art";
const CART_URL = "http://localhost:30025/api/cart";

const CustomerDashboard = () => {
  const [activeSection, setActiveSection] = useState("posts");
  const [arts, setArts] = useState([]);
  const [selectedArt, setSelectedArt] = useState(null);
  const [profile, setProfile] = useState(null);
  const [showProfile, setShowProfile] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [editUsername, setEditUsername] = useState("");
  const [cartItems, setCartItems] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const storedUser = localStorage.getItem("user");
    if (storedUser) setProfile(JSON.parse(storedUser));
  }, []);

  useEffect(() => {
    if (activeSection === "posts") {
      axios
        .get(`${API_URL}/all`)   // ✅ now hits correct port 5000
        .then((res) => setArts(res.data))
        .catch((err) => console.error("Error fetching arts:", err));
    }
    if (activeSection === "cart") {
      fetchCartItems();
    }
  }, [activeSection]);

  const fetchCartItems = () => {
    axios
      .get(`${CART_URL}/all`)  // ✅ change from 30025 → 5000
      .then((res) => setCartItems(res.data))
      .catch((err) => console.error("Error fetching cart items:", err));
  };

  const handleLogout = () => {
    localStorage.removeItem("user");
    navigate("/");
  };

  const handleSaveUsername = () => {
    const updatedProfile = { ...profile, username: editUsername };
    setProfile(updatedProfile);
    localStorage.setItem("user", JSON.stringify(updatedProfile));
    setIsEditing(false);
    setShowProfile(false);
  };

  const handleAddToCart = async (art) => {
    try {
      const formData = new FormData();
      formData.append("artName", art.artName);
      formData.append("artDescription", art.artDescription);
      formData.append("artistName", art.artistName);
      formData.append("artCost", art.artCost);

      const imageResponse = await axios.get(
        `${API_URL}/${art.id}/image`,  // ✅ now fetches from port 5000
        { responseType: "blob" }
      );

      formData.append("artPicture", new File([imageResponse.data], "image.jpg"));

      await axios.post(`${CART_URL}/add`, formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });

      alert("Added to cart!");
    } catch (error) {
      console.error("Error adding to cart:", error);
      alert("Failed to add to cart.");
    }
  };

  const handleDeleteCartItem = async (id) => {
    try {
      await axios.delete(`${CART_URL}/delete/${id}`);  // ✅ 5000 fixed
      fetchCartItems();
      alert("Item removed from cart.");
    } catch (error) {
      console.error("Error deleting cart item:", error);
      alert("Failed to remove item.");
    }
  };

  const renderPosts = () => {
    if (selectedArt) {
      return (
        <div className="artwork-detail">
          <img
            src={`${API_URL}/${selectedArt.id}/image`} // ✅ fixed URL
            alt={selectedArt.artName}
            className="detail-img"
          />
          <h2>{selectedArt.artName}</h2>
          <p>{selectedArt.artDescription}</p>
          <p><strong>Donor:</strong> {selectedArt.artistName}</p>
          <p><strong>Age:</strong> {selectedArt.artCost}</p>
          <button className="cart-btn" onClick={() => handleAddToCart(selectedArt)}>Required Donors</button>
          <button className="back-btn" onClick={() => setSelectedArt(null)}>Back</button>
        </div>
      );
    }

    return (
      <div className="posts-page">
        <h2 className="section-title">All Posts</h2>
        <div className="artworks-grid">
          {arts.length > 0 ? (
            arts.map((art) => (
              <div key={art.id} className="art-card" onClick={() => setSelectedArt(art)}>
                <img
                  src={`${API_URL}/${art.id}/image`}  // ✅ fixed port URL
                  alt={art.artName}
                />
                <h3>{art.artName}</h3>
                <p>{art.artistName}</p>
                <p>{art.artCost}</p>
              </div>
            ))
          ) : (
            <p>No artworks available.</p>
          )}
        </div>
      </div>
    );
  };

  const renderCart = () => (
    <div className="cart-page">
      <h2 className="section-title">require Donors List </h2>
      <div className="artworks-grid">
        {cartItems.length > 0 ? (
          cartItems.map((item) => (
            <div key={item.id} className="art-card">
              <img src={`${CART_URL}/${item.id}/image`} alt={item.artName} />
              <h3>{item.artName}</h3>
              <p>{item.artistName}</p>
              <p>${item.artCost}</p>
              <p>{item.artDescription}</p>
              <button className="delete-btn" onClick={() => handleDeleteCartItem(item.id)}>Delete</button>
            </div>
          ))
        ) : (
          <p>No items in cart.</p>
        )}
      </div>
    </div>
  );

  return (
    <div className="customer-dashboard">
      <aside className="sidebar">
        <h1>Customer Dashboard</h1>
        <nav className="nav-links">
          <button className={activeSection === "posts" ? "active" : ""} onClick={() => setActiveSection("posts")}>Posts</button>
          <button className={activeSection === "cart" ? "active" : ""} onClick={() => setActiveSection("cart")}>required donors list</button>
        </nav>

        <div className="sidebar-bottom">
          <div className="profile-card" onClick={() => setShowProfile(true)} style={{ cursor: "pointer" }}>
            {profile?.image ? (
              <img src={profile.image} alt="Profile" className="profile-avatar" />
            ) : (
              <FaUserCircle size={40} />
            )}
            <div className="profile-info">
              <p className="name">{profile?.username || "Guest"}</p>
              <p className="role">{profile?.role || "Unknown Role"}</p>
              <p className="email">{profile?.email}</p>
            </div>
          </div>

          <button className="logout-btn" onClick={handleLogout}>
            <FaSignOutAlt style={{ marginRight: "8px" }} /> Logout
          </button>
        </div>
      </aside>

      <main className="dashboard-main">
        {activeSection === "posts" && renderPosts()}
        {activeSection === "cart" && renderCart()}
      </main>

      {showProfile && profile && (
        <div className="popup-overlay">
          <div className="popup-card">
            <h2>Profile</h2>
            <p><strong>Email:</strong> {profile.email}</p>
            <p><strong>Role:</strong> {profile.role}</p>
            <p>
              <strong>Username:</strong>
              {isEditing ? (
                <>
                  <input type="text" value={editUsername} onChange={(e) => setEditUsername(e.target.value)} />
                  <button onClick={handleSaveUsername}>Save</button>
                  <button onClick={() => setIsEditing(false)}>Cancel</button>
                </>
              ) : (
                <>
                  {profile.username}
                  <button onClick={() => { setEditUsername(profile.username); setIsEditing(true); }}>Edit</button>
                </>
              )}
            </p>
            <button onClick={() => setShowProfile(false)}>Close</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default CustomerDashboard;
