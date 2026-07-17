<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>SIGN UP | FITCORE</title>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<style>
* { margin: 0; padding: 0; box-sizing: border-box; font-family: Arial, sans-serif; }
body { min-height: 100vh; display: flex; align-items: center; justify-content: center; position: relative; padding: 28px 14px; }
body::before { content: ""; position: fixed; inset: 0; background: linear-gradient(rgba(31,41,55,.55), rgba(31,41,55,.75)), url("<%=request.getContextPath()%>/images/auth-bg.jpg") center/cover no-repeat; filter: blur(6px); transform: scale(1.04); z-index: -2; }
body::after { content: ""; position: fixed; inset: 0; background: rgba(244,246,249,.18); z-index: -1; }
.signup-wrapper { width: 100%; max-width: 480px; background: rgba(255,255,255,.96); padding: 35px; border-radius: 14px; box-shadow: 0 2px 18px rgba(0,0,0,.2); }
.logo { text-align: center; font-size: 30px; font-weight: bold; color: #1f2937; margin-bottom: 10px; }
.subtitle { text-align: center; color: #6b7280; margin-bottom: 28px; }
label { display: block; margin-bottom: 6px; font-weight: bold; color: #374151; }
input, select { width: 100%; padding: 12px; margin-bottom: 16px; border: 1px solid #d1d5db; border-radius: 8px; outline: none; }
input:focus, select:focus { border-color: #2563eb; }
.password-wrap { position: relative; }
.password-wrap input { padding-right: 72px; }
.toggle-password { position: absolute; right: 10px; top: 10px; border: none; background: transparent; color: #2563eb; cursor: pointer; font-weight: bold; }
.password-rule { color: #6b7280; font-size: 12px; line-height: 1.45; margin: -8px 0 16px; }
.btn-signup, .btn-reset { width: 100%; color: white; padding: 12px; border: none; border-radius: 8px; cursor: pointer; margin-bottom: 12px; }
.btn-signup { background: #2563eb; font-weight: bold; }
.btn-signup:hover { background: #1d4ed8; }
.btn-reset { background: #6b7280; margin-bottom: 18px; }
.links { text-align: center; font-size: 14px; }
.links a { color: #2563eb; text-decoration: none; }
.links a:hover { text-decoration: underline; }
</style>
</head>
<body>
<div class="signup-wrapper">
    <div class="logo">FitCore</div>
    <p class="subtitle">Create Staff Account</p>

    <c:if test="${not empty errorMessage}">
        <div id="server-error" hidden><c:out value="${errorMessage}" /></div>
    </c:if>

    <form id="signupForm" action="<%=request.getContextPath()%>/SignUpServlet" method="post" novalidate>
        <label for="staffName">Full Name</label>
        <input id="staffName" type="text" name="staffName" value="<c:out value='${enteredName}'/>" placeholder="Ahmad" required>

        <label for="staffPhone">Phone Number</label>
        <input id="staffPhone" type="text" name="staffPhone" value="<c:out value='${enteredPhone}'/>" placeholder="0123456789" required>

        <label for="staffEmail">Email</label>
        <input id="staffEmail" type="email" name="staffEmail" value="<c:out value='${enteredEmail}'/>" placeholder="ahmad@gmail.com" required>

        <label for="staffUsername">Username</label>
        <input id="staffUsername" type="text" name="staffUsername" value="<c:out value='${enteredUsername}'/>" required>

        <label for="staffPassword">Password</label>
        <div class="password-wrap">
            <input id="staffPassword" type="password" name="staffPassword" required>
            <button type="button" class="toggle-password" data-target="staffPassword">Show</button>
        </div>
        <p class="password-rule">8–20 characters with uppercase, lowercase, number, special character, and no spaces.</p>

        <label for="confirmPassword">Confirm Password</label>
        <div class="password-wrap">
            <input id="confirmPassword" type="password" name="confirmPassword" required>
            <button type="button" class="toggle-password" data-target="confirmPassword">Show</button>
        </div>

        <label for="staffRole">Role</label>
        <select id="staffRole" name="staffRole" required>
            <option value="Staff" ${enteredRole == 'Staff' ? 'selected' : ''}>Staff</option>
            <option value="Trainer" ${enteredRole == 'Trainer' ? 'selected' : ''}>Trainer</option>
            <option value="Receptionist" ${enteredRole == 'Receptionist' ? 'selected' : ''}>Receptionist</option>
            <option value="Manager" ${enteredRole == 'Manager' ? 'selected' : ''}>Manager</option>
        </select>

        <button type="submit" class="btn-signup">Sign Up</button>
        <button type="reset" class="btn-reset">Reset</button>

        <div class="links">
            <a href="<%=request.getContextPath()%>/login.jsp">Already have an account? Login</a><br><br>
            <a href="<%=request.getContextPath()%>/index.jsp">Back to Home</a>
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

document.getElementById('signupForm').addEventListener('submit', function (event) {
    const requiredIds = ['staffName', 'staffPhone', 'staffEmail', 'staffUsername', 'staffPassword', 'confirmPassword'];
    const missing = requiredIds.some(function (id) { return !document.getElementById(id).value.trim(); });
    const email = document.getElementById('staffEmail');
    const password = document.getElementById('staffPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (missing) {
        event.preventDefault();
        Swal.fire({ icon: 'warning', title: 'Incomplete Form', text: 'Please complete all required fields.', confirmButtonColor: '#2563eb' });
        return;
    }

    if (!email.checkValidity()) {
        event.preventDefault();
        Swal.fire({ icon: 'warning', title: 'Invalid Email', text: 'Please enter a valid email address.', confirmButtonColor: '#2563eb' });
        return;
    }

    if (!strongPassword.test(password)) {
        event.preventDefault();
        Swal.fire({
            icon: 'warning',
            title: 'Weak Password',
            text: 'Password must be 8 to 20 characters and include uppercase, lowercase, number, special character, and no spaces.',
            confirmButtonColor: '#2563eb'
        });
        return;
    }

    if (password !== confirmPassword) {
        event.preventDefault();
        Swal.fire({ icon: 'warning', title: 'Passwords Do Not Match', text: 'Enter the same password in both password fields.', confirmButtonColor: '#2563eb' });
    }
});

const serverError = document.getElementById('server-error');
if (serverError) {
    Swal.fire({ icon: 'error', title: 'Registration Failed', text: serverError.textContent.trim(), confirmButtonColor: '#2563eb' });
}
</script>
</body>
</html>
