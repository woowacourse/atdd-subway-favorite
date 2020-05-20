const METHOD = {
  GET(data) {
    return {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${data}`
      },
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
  }
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
      return requestWithJsonData(`/me/bearer`, METHOD.GET(token))
    }
  }


  return {
    line,
    path,
    member,
    login
  }
})()

export default api
