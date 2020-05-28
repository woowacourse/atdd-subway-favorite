import { request, requestWithJsonData } from './request.js'

export default (() => {
  let headers = {}
  let active = false

  const login = ({ accessToken, tokenType }) => {
    headers = {
      Authorization: `${tokenType} ${accessToken}`
    }
    localStorage.setItem('access_token', accessToken)
    localStorage.setItem('token_type', tokenType)
    active = true
  }

  const logout = () => {
    localStorage.clear()
    active = false
    headers = {}
  }

  const getInfo = () => requestWithJsonData('/me', {
    headers
  }).catch(e => {
    logout()
    throw new Error("정보를 불러오는데 실패했습니다.")
  })

  const update = (data) => request('/me', {
    headers: {
      ...headers,
      'Content-Type': 'application/json'
    },
    method: "PATCH",
    body: JSON.stringify(data)
  }).catch(() => {
    throw new Error("회원 정보 수정에 실패했습니다.")
  })

  const deleteAccount = () => request("/me", {
    headers,
    method: "DELETE"
  }).catch(() => {
    throw new Error("회원 탈퇴에 실패했습니다.")
  }).then(() => {
    logout()
  })

  const addFavorite = data => request("/me/favorites", {
    headers: {
      ...headers,
      'Content-Type': 'application/json'
    },
    method: "POST",
    body: JSON.stringify(data)
  }).catch(() => {
    throw new Error("즐겨찾기 추가에 실패했습니다.")
  })

  const getFavorites = () => requestWithJsonData("/me/favorites", {
    headers,
  }).catch(() => {
    throw new Error("즐겨찾기를 불러오는데 실패했습니다.")
  })

  const hasFavorite = ({ sourceId, targetId }) => requestWithJsonData(`/me/favorites/from/${sourceId}/to/${targetId}`,
    {
      headers,
    }).catch(() => {
    throw new Error("즐겨찾기를 불러오는데 실패했습니다.")
  })

  const deleteFavorite = ({ sourceId, targetId }) => request(`/me/favorites/from/${sourceId}/to/${targetId}`,
    {
      headers,
      method: 'DELETE'
    }).catch(() => {
    throw new Error("즐겨찾기를 삭제하는데 실패했습니다.")
  })

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
    logout,
    getInfo,
    isLoggedIn,
    update,
    deleteAccount,
    addFavorite,
    getFavorites,
    hasFavorite,
    deleteFavorite
  }
})()
