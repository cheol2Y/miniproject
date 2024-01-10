function checkLoginStatusAndUpdateUI() {
    fetch('/api/user/status')
        .then(response => response.json())
        .then(data => {
            updateAuthButton(data.loggedIn);
        })
        .catch(error => console.error('Error:', error));
}

function updateAuthButton(isLoggedIn) {
    var authButton = document.getElementById('authButton');
    if (isLoggedIn) {
        authButton.href = 'logoutAction'; 
        authButton.textContent = '로그아웃';
    } else {
        authButton.href = 'loginForm'; 
        authButton.textContent = '로그인';
    }
}
function loginClicked() {
    console.log("로그인 버튼이 클릭되었습니다.");
}

window.onload = function () {
    checkLoginStatusAndUpdateUI();

    const urlParams = new URLSearchParams(window.location.search);
    const loginError = urlParams.get('error');
    if (loginError === 'true') {
        alert('아이디 혹은 패스워드가 틀렸습니다.');
    }
    const switchers = [...document.querySelectorAll('.switcher')];
    switchers.forEach(item => {
        item.addEventListener('click', function() {
            switchers.forEach(item => item.parentElement.classList.remove('is-active'));
            this.parentElement.classList.add('is-active');
        });
    });
    const passwordField = document.getElementById('signup-password');
    const confirmPasswordField = document.getElementById('signup-confirm-password');
    const passwordMatchMessage = document.getElementById('password-match-message');
    const signupButton = document.getElementById('signup-button');

    signupButton.addEventListener('click', function(e) {
        const password = passwordField.value;
        const confirmPassword = confirmPasswordField.value;

        if (password !== confirmPassword) {
            e.preventDefault();
            passwordMatchMessage.textContent = '비밀번호가 일치하지 않습니다.';
            alert("비밀번호가 일치하지 않습니다.");
        } else {
            passwordMatchMessage.textContent = '비밀번호가 일치합니다.';
        }
    });
};