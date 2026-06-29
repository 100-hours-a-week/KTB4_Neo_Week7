const API_BASE_URL = "http://localhost:8080";
const DEFAULT_PROFILE_IMAGE = "./assets/images/default-profile.svg";
const DEFAULT_POST_IMAGE = "./assets/images/default-post.svg";

function getAccessToken() {
  return localStorage.getItem("accessToken");
}

function getLoginUserId() {
  return localStorage.getItem("userId") || localStorage.getItem("loginUserId");
}

function requireLogin() {
  const accessToken = getAccessToken();

  if (!accessToken) {
    window.location.href = "./login.html";
    throw new Error("unauthorized_user");
  }

  return accessToken;
}

function resolveImageUrl(imageUrl, fallbackUrl = "") {
  if (!imageUrl) return fallbackUrl;
  if (/^(https?:|data:|blob:)/.test(imageUrl)) return imageUrl;
  if (imageUrl.startsWith("./") || imageUrl.startsWith("../")) return imageUrl;
  if (imageUrl.startsWith("/")) return `${API_BASE_URL}${imageUrl}`;
  return `${API_BASE_URL}/${imageUrl}`;
}

async function uploadImageFile(file) {
  if (!file) return "";

  const formData = new FormData();
  formData.append("image", file);

  const response = await fetch(`${API_BASE_URL}/uploads/images`, {
    method: "POST",
    headers: {
      ...(getAccessToken() ? { Authorization: `Bearer ${getAccessToken()}` } : {}),
    },
    body: formData,
  });

  const result = await response.json().catch(() => null);

  if (!response.ok) {
    throw new Error(result?.message || "이미지 업로드에 실패했습니다.");
  }

  return result.data.imageUrl;
}
