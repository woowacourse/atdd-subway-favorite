const METHOD = {
  AUTHORIZED_GET() {
    return {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem("jwt") || ""
      }
    }
  },
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
      return request(`/members`, METHOD.POST(data))
    },
    find(email) {
      return request(`/members?email=${email}`)
    },
    update(id, data) {
      return requestWithJsonData(`/members/${id}`, METHOD.PUT(data))
    },
    delete(id) {
      return requestWithJsonData(`/members/${id}`, METHOD.DELETE())
    },
    login(data) {
      return requestWithJsonData('/login', METHOD.POST(data))
    },
    myinfo() {
      return requestWithJsonData('/myinfo', METHOD.AUTHORIZED_GET())
    }
  }

  return {
    line,
    path,
    member
  }
})()

export default api
