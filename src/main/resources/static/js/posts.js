async function loadPosts() {
    try {
        const res = await fetch("/api/posts");
        const posts = await res.json();
        renderCards(posts);
    } catch (err) {
        console.error("불러오기 실패:", err);
    }
}

function renderCards(posts) {
    const cardHTML = posts.map(post => `
    <div class="col">
        <div class="card h-100 shadow-sm">
        <div class="card-body">
            <h5 class="card-title">${post.title}</h5>
            <p class="card-text text-muted">작성자: ${post.author}</p>
        </div>
        <div class="card-footer text-muted">
            ${new Date(post.createdAt).toLocaleString()}
        </div>
        </div>
    </div>
    `).join("");
    document.getElementById("cardView").innerHTML = cardHTML;
}

const token = localStorage.getItem("accessToken");
const loginBtn = document.querySelector(".btn-outline-primary");
const logoutBtn = document.querySelector(".btn-outline-danger");

if(token){
    loginBtn.style.display = "none";
    logoutBtn.style.display = "block";
} else{
    loginBtn.style.display = "block";
    logoutBtn.style.display = "none";
}

logoutBtn.addEventListener("click", async (e) => {
    e.preventDefault();
    logout();
});

loadPosts();