const METHOD = {
  PUT() {
    return {
      method: 'PUT'
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
      return requestWithJsonData(`/members`, METHOD.POST(data));
    },
    getMemberByEmail() {
      return request(`/members`)
    },
    update(data, id) {
      return requestWithJsonData(`/members/${id}`, METHOD.PUT(data));
    },
    delete(id) {
      return request(`/members/${id}`);
    }
  }

  const login = {
    createToken(data) {
      return requestWithJsonData(`/oauth/token`, METHOD.POST(data));
    },
    loginWithIdPsw(data) {
      return requestWithJsonData(`/login`, METHOD.POST(data));
    },
    getMemberOfMine(data) {
      return requestWithJsonData(`/me/bearer`, METHOD.GET(data));
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
