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
  PUT(data) {
    return {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem("jwt") || ""
      },
      body: JSON.stringify({
        ...data
      })
    }
  },
  DELETE() {
    return {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem("jwt") || ""
      }
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
  const requestWithJsonData = (uri, config) => fetch(uri, config).then(async response => {
    if (!response.ok) {
      throw new Error((await response.json()).message);
    }
    return response.json();
  })

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
    update(data) {
      return request(`/members`, METHOD.PUT(data))
    },
    delete() {
      return request(`/members`, METHOD.DELETE())
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
