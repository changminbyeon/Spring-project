function parseJwt(token) {
    try{
            const base64Url = token.split('.')[1];
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const jsonPayload = decodeURIComponent(
                atob(base64)
                .split('')
                .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                .join('')
            );

            return JSON.parse(jsonPayload);
    } catch(e){
        console.warn("Invalid JWT: ", e);
        return null;
    }
}

function isTokenExpired(token){
    const payload = parseJwt(token);
    if(!payload || !payload.exp) return true;

    const now = Math.floor(Date.now() / 1000);
    return payload. exp < now;
}

function checkTokenAndRedirect(){
    const token = localStorage.getItem("accessToken");
    if(!token || isTokenExpired(token)){
        localStorage.removeItem("accessToken");
        alert("로그인이 만료되었거나 유효하지 않습니다.");
        window.location.href = "/login.html";
        return false;
    }
    return true;
}

async function fetchWithAuth(url, options = {}){
    const token = localStorage.getItem("accessToken");
    if(!token || isTokenExpired(token)){
    checkTokenAndRedirect();
    return null;
    }

    const headers = {
        ...options.headers,
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
    };

    try{
            const response = await fetch(url, {
                ...options,
                headers
            });

            if(response.status == 401){
                checkTokenAndRedirect();
                return null;
            }

            return response;
    } catch(err){
        console.error("요청 실패:", err);
        alert("서버 오류가 발생했습니다.");
        return null;
    }
}

async function logout() {
    const token = localStorage.getItem("accessToken");
    try {
            const response = await fetch("/api/users/logout", {
                method: "POST",
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            });

            if(response.ok){
                localStorage.removeItem("accessToken");
                alert("로그아웃 되었습니다.");
                window.location.href = "/index.html";
            } else{
                alert("로그아웃 실패");
            }

    } catch(error){
        console.error("로그아웃 오류: ", error);
        alert("에러가 발생했습니다.");
    }
}