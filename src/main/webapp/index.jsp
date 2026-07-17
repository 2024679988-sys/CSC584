<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>FitCore | Gym Management System</title>

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>

<link href="https://fonts.googleapis.com/css2?family=Arial:wght@400;700&family=Bebas+Neue&display=swap" rel="stylesheet">

<style>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-family: Arial, Helvetica, sans-serif;
}

body {
    background: #111827;
    color: white;
}

.hero {
    min-height: 100vh;
    background:
        linear-gradient(rgba(0,0,0,0.68), rgba(0,0,0,0.75)),
        url("<%= request.getContextPath() %>/images/hero.jpg") center/cover no-repeat;
    position: relative;
    overflow: hidden;
}

.navbar {
    width: 100%;
    padding: 35px 60px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.logo {
    font-size: 30px;
    font-weight: bold;
}

.logo span {
    color: #2563eb;
}

.nav-right {
    display: flex;
    align-items: center;
    gap: 18px;
}

.nav-right a {
    color: white;
    text-decoration: none;
}

.login {
    padding: 12px 30px;
    background: #1f2937;
    border: 1px solid #374151;
    border-radius: 30px;
    font-weight: bold;
}

.login:hover {
    background: #374151;
}

.signup {
    padding: 12px 30px;
    background: #2563eb;
    border-radius: 30px;
    font-weight: bold;
}

.signup:hover {
    background: #1d4ed8;
}

.menu-icon {
    font-size: 30px;
    margin-left: 18px;
}

.hero-content {
    padding: 110px 70px 0;
    max-width: 760px;
}

.hero-content h1{
    font-family: "Bebas Neue", sans-serif;
    font-size: 95px;
    line-height: 0.95;
    letter-spacing: 3px;
    font-weight: 400;
    margin-bottom: 25px;
    text-transform: uppercase;
}

.hero-content p {
    color: #d1d5db;
    font-size: 20px;
    line-height: 1.7;
    max-width: 600px;
}

.hero-btn {
    display: inline-block;
    margin-top: 35px;
    background: #2563eb;
    color: white;
    text-decoration: none;
    padding: 15px 36px;
    border-radius: 30px;
    font-weight: bold;
}

.hero-btn:hover {
    background: #1d4ed8;
}

.bottom-stats {
    position: absolute;
    left: 70px;
    right: 70px;
    bottom: 60px;
    border-top: 1px solid rgba(255,255,255,0.18);
    border-bottom: 1px solid rgba(255,255,255,0.18);
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    background: rgba(0,0,0,0.35);
    backdrop-filter: blur(8px);
}

.stat-card {
    padding: 22px 28px;
    border-right: 1px solid rgba(255,255,255,0.15);
}

.stat-card:last-child {
    border-right: none;
}

.stat-title {
    color: #d1d5db;
    font-size: 14px;
    margin-bottom: 8px;
}

.stat-value {
    font-size: 28px;
    font-weight: bold;
}

.stat-sub {
    color: #2563eb;
    font-size: 14px;
    margin-top: 6px;
}

@media (max-width: 900px) {
    .navbar {
        padding: 25px;
    }

    .hero-content {
        padding: 70px 25px 0;
    }

    .hero-content h1 {
        font-size: 46px;
    }

    .bottom-stats {
        grid-template-columns: 1fr;
        position: static;
        margin: 50px 25px;
    }

    .stat-card {
        border-right: none;
        border-bottom: 1px solid rgba(255,255,255,0.15);
    }
}
</style>
</head>

<body>

<div class="hero">

    <div class="navbar">
        <div class="logo">
            <span>●</span> FitCore
        </div>

        <div class="nav-right">
            <a href="<%= request.getContextPath() %>/login.jsp" class="login">Login</a>
            <a href="<%= request.getContextPath() %>/signup.jsp" class="signup">Sign Up</a>
            <div class="menu-icon">☰</div>
        </div>
    </div>

    <div class="hero-content">
        <h1>
            Stronger gym,<br>
            smarter system,<br>
            easier management.<br><br><br>
        </h1>

        <a href="<%= request.getContextPath() %>/login.jsp" class="hero-btn">
            Get Started
        </a>
    </div>

    <div class="bottom-stats">
        <div class="stat-card">
            <div class="stat-title">MEMBERS UP TO</div>
            <div class="stat-value">120</div>
            <div class="stat-sub">Active gym members</div>
        </div>

        <div class="stat-card">
            <div class="stat-title">TOTAL EQUIPMENT</div>
            <div class="stat-value">32</div>
            <div class="stat-sub">Available equipment records</div>
        </div>

        <div class="stat-card">
            <div class="stat-title">RECENT PROMOTION</div>
            <div class="stat-value">Member Special</div>
            <div class="stat-sub">15% discount</div>
        </div>
    </div>

</div>

</body>
</html>