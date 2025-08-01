document.addEventListener('DOMContentLoaded', async () => {
    const token = localStorage.getItem('accessToken');
    if(!token){
        alert('로그인이 필요합니다.');
        window.location.href="./login.html";
        return;
    }

    const urlParms = new URLSearchParams(window.location.search);
    const postId = urlParms.get('id');

    if(!postId){
        alert('잘못된 접근입니다.');
        window.location.href="/posts.html";
        return;
    }

    try {
            const res = await fetch(`/api/posts/${postId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if(!res.ok){
                const error = await res.json();
                alert('에러: ' + error.message);
                return;
            }

            const post = await res.json();
            document.getElementById('post-title').textContent = post.title;
            document.getElementById('post-content').textContent = post.content;

            const username = parseJwt(token)?.sub;
            if(post.author == username){
                document.getElementById('edit-btn').style.display = 'inline-block';
                document.getElementById('delete-btn').style.display = 'inline-block';
            }

            document.getElementById('edit-btn').addEventListener('click', () => {
                 document.getElementById('edit-btn').value = post.tile;
                 document.getElementById('edit-content').value = post.content;
                 document.getElementById('edit-form').style.display = 'block';

                 document.getElementById('cancel-btn').addEventListener('click', () => {
                         document.getElementById('edit-form').style.display = 'none';
                 });

                 document.getElementById('save-btn').addEventListener('click', async () => {
                    const newTitle = document.getElementById('edit-title').value;
                    const newContent = document.getElementById('edit-content').value;

                    const updateRes = await fetch(`/api/posts/${postId}`, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${token}`
                        }
                    });

                    if(updateRes.ok){
                        alert('수정 완료');
                        location.reload();
                    }   else{
                        const err = await updateRes.json();
                        alert('수정 실패: ' + err.message);
                    }
                 });
            }

            document.getElementById('delete-btn').addEventListener('click', async () => {
                if (!confirm('정말 삭제하시겠습니까?')) return;

                const delRes = await fetch(`/api/posts/${postId}`, {
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                if (delRes.ok) {
                    alert('삭제 완료');
                    window.location.href = './posts.html';
                }
                else{
                    alert('삭제 실패');
                }
            });

    }   catch (err) {
        alert('서버 오류: ' + err.message);
    }
});