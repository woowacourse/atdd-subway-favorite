const METHOD = {
  GET() {
    return {
      method: 'GET'
    }
  },
  PUT(data) {
    return {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        ...data
      })
    }
  },
  DELETE() {
    return {
      method: 'DELETE'
    }
  },
  POST(data) {
    return {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        ...data
      })
    }
  },
  GET_WITH_TOKEN(token) {
    return {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`
      },
    }
  },
  POST_WITH_TOKEN(token, data) {
    return {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        ...data
      })
    }
  },
  DELETE_WITH_TOKEN(token) {
    return {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    }
  },
}

const api = (() => {
  const request = (uri, config) => fetch(uri, config)
  const requestWithJsonData = (uri, config) => fetch(uri, config).then(data => data.json())

  const line = {
    getAll() {
      return request(`/lines/detail`)
    },
    getAllDetail() {
      return requestWithJsonData(`/lines/detail`)
    }
  }

  const path = {
    find(params) {
      return requestWithJsonData(`/paths?source=${params.source}&target=${params.target}&type=${params.type}`)
    }
  }

  const member = {
    create(data) {
      request(`/members`, METHOD.POST(data))
    },
    update(id, data) {
      request(`/members/${id}`, METHOD.PUT(data))
    },
    delete(id) {
      request(`/members/${id}`, METHOD.DELETE())
    }
  }

  const login = {
    login(data) {
      return requestWithJsonData(`/oauth/token`, METHOD.POST(data))
    },
    getUserInfo(token) {
      return requestWithJsonData(`/me/bearer`, METHOD.GET_WITH_TOKEN(token))
    }
  }

  const favorite = {
    create(token,data) {
      console.log(data)
      return request(`/members/favorite`, METHOD.POST_WITH_TOKEN(token,data))
    },
    getFavoriteByMember(token) {
      return requestWithJsonData(`/members/favorite`, METHOD.GET_WITH_TOKEN(token))
    },
    delete(token, favoriteId) {
      return requestWithJsonData(`/members/favorite/${favoriteId}`, METHOD.DELETE_WITH_TOKEN(token))
    }
  }


  return {
    line,
    path,
    member,
    login,
    favorite
  }
})()

export default api
