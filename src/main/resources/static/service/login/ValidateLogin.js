export const validateLogin = () => {
    const token = sessionStorage.getItem("accessToken")

    if (token === null) {
        alert("로그인 해주세요.")
        window.location = "/login"
    }
}

export const validateNotLogin = () => {
    const token = sessionStorage.getItem("accessToken")
    if (token) {
        window.location = "/service"
    }
}