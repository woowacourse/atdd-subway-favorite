export default (() => {
  let headers = {}
  let active = false

  const login = ({ accessToken, tokenType }) => {
    console.log(accessToken, tokenType)
    headers = {
      Authorization: `${tokenType} ${accessToken}`
    }
    localStorage.setItem('access_token', accessToken)
    localStorage.setItem('token_type', tokenType)
    active = true
  }

  const isLoggedIn = () => active

  const init = () => {
    const accessToken = localStorage.getItem('access_token')
    const tokenType = localStorage.getItem('token_type')

    if (accessToken && tokenType) {
      login({ accessToken, tokenType })
    }
  }

  init()

  return {
    login,
    isLoggedIn
  }
})()
