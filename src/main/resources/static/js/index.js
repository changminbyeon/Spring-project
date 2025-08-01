window.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("accessToken");

    const loginBtn = document.querySelector(".btn-primary");
    const signupBtn = document.querySelector(".btn-secondary");
    const logoutBtn = document.querySelector(".btn-danger");

    if(token){
        loginBtn.style.display = "none";
        signupBtn.style.display = "none";
        logoutBtn.style.display = "block";
    } else{
            loginBtn.style.display = "block";
            signupBtn.style.display = "block";
            logoutBtn.style.display = "none";
    }

    logoutBtn.addEventListener("click", async (e) => {
        e.preventDefault();
        logout();
    });
});