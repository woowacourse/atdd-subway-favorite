const METHOD = {
  GET_WITH_AUTH(token) {
    return {
      method: 'GET',
      headers: {
        'Authorization': token,
      }
    }
  },
  PUT(token, data) {
    return {
      method: 'PUT',
      headers: {
        'Authorization': token,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        ...data
      })
    }
  },
  DELETE(id, token) {
    return {
      method: 'DELETE',
      headers: {
        'Authorization': token,
      },
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
  POST_WITH_AUTH(token, data) {
    return {
      method: 'POST',
      headers: {
        'Authorization': token,
        'Content-Type': 'application/json',
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
        'Authorization': token,
      }
    }
  }
}

const api = (() => {
  const request = (uri, config) => fetch(uri, config).then(data => data)
  const requestWithJsonData = async (uri, config) => {
    try {
      const response = await fetch(uri, config)
      if (response.status === 403) {
        const error = await response.json()
        alert(error.message)
        window.location.href = "login"
        return
      }
      return await response.json()
    }
    catch (error) {
      alert(error.message)
    }
  }

  const line = {
    getAll() {
      return request(`/lines/detail`)
    },
    getAllDetail() {
      return requestWithJsonData(`/lines/detail`)
    }
  }

  const station = {
    getAll() {
      return requestWithJsonData(`/stations`)
    },
  }

  const path = {
    find(params) {
      return requestWithJsonData(`/paths?source=${params.source}&target=${params.target}&type=${params.type}`)
    }
  }

  const member = {
    create(data) {
      return request(`/members`, METHOD.POST(data))
    },
    login(data) {
      return request(`/login`, METHOD.POST(data))
    },
    getWithAuth(token) {
      return requestWithJsonData(`/members`, METHOD.GET_WITH_AUTH(token))
    },
    update(id, token, data) {
      return request(`/members/${id}`, METHOD.PUT(token, data))
    },
    delete(id, token) {
      return request(`/members/${id}`, METHOD.DELETE(id, token))
    },
    addFavorite(token, data) {
      return request(`/members/favorites`, METHOD.POST_WITH_AUTH(token, data))
    },
    findFavorites(token) {
      return requestWithJsonData(`/members/favorites`, METHOD.GET_WITH_AUTH(token))
    },
    deleteFavorite(token, id) {
      return request(`/members/favorites/${id}`, METHOD.DELETE_WITH_TOKEN(token, id))
    }
  }

  return {
    line,
    station,
    path,
    member
  }
})
()

export default api
