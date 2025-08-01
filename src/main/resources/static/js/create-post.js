window.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("accessToken");
    if (!token) {
        alert("로그인을 해주세요.");
        window.location.href = '/login.html';
        return;
    }

    const form = document.getElementById("post-form");
    const message = document.getElementById("message");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const title = document.getElementById("title").value;
        const content = document.getElementById("content").value;

        try {
                const res = await fetch("/api/posts", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${token}`
                    },
                    body: JSON.stringify({ title, content })
                 });

                if (!res.ok) {
                  const err = await res.text();
                  throw new Error(err);
                }

                alert("게시글이 작성되었습니다!");
                window.location.href = "/posts.html";
        } catch (err) {
            message.textContent = "오류 발생: " + err.message;
            message.classList.remove("d-none");
        }
    });
});