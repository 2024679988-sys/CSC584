<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>FITCORE | LOGIN</title>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<style>
* { margin: 0; padding: 0; box-sizing: border-box; font-family: Arial, sans-serif; }
body { min-height: 100vh; display: flex; align-items: center; justify-content: center; position: relative; overflow: hidden; }
body::before { content: ""; position: absolute; inset: 0; background: linear-gradient(rgba(31,41,55,.55), rgba(31,41,55,.75)), url("<%=request.getContextPath()%>/images/auth-bg.jpg") center/cover no-repeat; filter: blur(7px); transform: scale(1.05); z-index: -2; }
body::after { content: ""; position: absolute; inset: 0; background: rgba(255,255,255,.08); z-index: -1; }
.login-wrapper { width: 100%; max-width: 420px; background: rgba(255,255,255,.95); backdrop-filter: blur(15px); padding: 35px; border-radius: 18px; box-shadow: 0 15px 40px rgba(0,0,0,.25); }
.logo { text-align: center; font-size: 34px; font-weight: bold; color: #1f2937; margin-bottom: 10px; }
.logo span { color: #2563eb; }
.subtitle { text-align: center; color: #6b7280; margin-bottom: 28px; }
label { display: block; margin-bottom: 6px; font-weight: bold; color: #374151; }
input { width: 100%; padding: 12px; margin-bottom: 18px; border: 1px solid #d1d5db; border-radius: 8px; outline: none; }
input:focus { border-color: #2563eb; }
.password-wrap { position: relative; }
.password-wrap input { padding-right: 72px; }
.toggle-password { position: absolute; right: 10px; top: 10px; border: none; background: transparent; color: #2563eb; cursor: pointer; font-weight: bold; }
.password-rule { color: #6b7280; font-size: 12px; line-height: 1.45; margin: -10px 0 18px; }
.btn-login, .btn-reset { width: 100%; color: white; padding: 12px; border: none; border-radius: 8px; cursor: pointer; margin-bottom: 12px; }
.btn-login { background: #2563eb; font-weight: bold; }
.btn-login:hover { background: #1d4ed8; }
.btn-reset { background: #6b7280; margin-bottom: 18px; }
.links { text-align: center; font-size: 14px; }
.links a { color: #2563eb; text-decoration: none; }
.links a:hover { text-decoration: underline; }
</style>
</head>
<body>
<div class="login-wrapper">
    <div class="logo"><span>●</span> FitCore</div>
    <p class="subtitle">Staff Login</p>

    <c:if test="${not empty errorMessage}">
        <div id="server-error" hidden><c:out value="${errorMessage}" /></div>
    </c:if>
    <c:if test="${not empty successMessage}">
        <div id="server-success" hidden><c:out value="${successMessage}" /></div>
    </c:if>

    <form id="loginForm" action="<%=request.getContextPath()%>/LoginController" method="post" novalidate>
        <label for="staffUsername">Username</label>
        <input id="staffUsername" type="text" name="staffUsername" value="<c:out value='${enteredUsername}'/>" required>

        <label for="staffPassword">Password</label>
        <div class="password-wrap">
            <input id="staffPassword" type="password" name="staffPassword" required>
            <button type="button" class="toggle-password" data-target="staffPassword">Show</button>
        </div>
        <p class="password-rule">8–20 characters with uppercase, lowercase, number, special character, and no spaces.</p>

        <button type="submit" class="btn-login">Login</button>
        <button type="reset" class="btn-reset">Reset</button>

        <div class="links">
            <a href="<%=request.getContextPath()%>/index.jsp">Back</a><br><br>
            <a href="<%=request.getContextPath()%>/signup.jsp">No account yet? Register</a>
        </div>
    </form>
</div>

<script>
const strongPassword = /^(?=\S{8,20}$)(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).*$/;

document.querySelectorAll('.toggle-password').forEach(function (button) {
    button.addEventListener('click', function () {
        const input = document.getElementById(button.dataset.target);
        const showing = input.type === 'text';
        input.type = showing ? 'password' : 'text';
        button.textContent = showing ? 'Show' : 'Hide';
    });
});

document.getElementById('loginForm').addEventListener('submit', function (event) {
    const username = document.getElementById('staffUsername').value.trim();
    const password = document.getElementById('staffPassword').value;

    if (!username) {
        event.preventDefault();
        Swal.fire({ icon: 'warning', title: 'Username Required', text: 'Please enter your username.', confirmButtonColor: '#2563eb' });
        return;
    }

    if (!strongPassword.test(password)) {
        event.preventDefault();
        Swal.fire({
            icon: 'warning',
            title: 'Invalid Password Format',
            text: 'Password must be 8 to 20 characters and include uppercase, lowercase, number, special character, and no spaces.',
            confirmButtonColor: '#2563eb'
        });
    }
});

const serverError = document.getElementById('server-error');
if (serverError) {
    Swal.fire({ icon: 'error', title: 'Login Failed', text: serverError.textContent.trim(), confirmButtonColor: '#2563eb' });
}

const serverSuccess = document.getElementById('server-success');
if (serverSuccess) {
    Swal.fire({ icon: 'success', title: 'Success', text: serverSuccess.textContent.trim(), confirmButtonColor: '#2563eb' });
}

const loginParams = new URLSearchParams(window.location.search);
if (loginParams.get('accountDeleted') === 'true') {
    Swal.fire({
        icon: 'success',
        title: 'Account Deleted',
        text: 'Your staff account was deleted successfully. You have been logged out.',
        confirmButtonColor: '#2563eb'
    });
}
</script>
</body>
</html>
