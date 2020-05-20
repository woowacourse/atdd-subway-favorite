const METHOD = {
  GET_WITH_AUTH() {
    return {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: localStorage.getItem("auth") || ""
      }
    }
  },
  PUT(data) {
    return {
      method: 'PUT',
      headers: {
        "Content-Type": "application/json",
        Authorization: localStorage.getItem("auth") || ""
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
    create(params) {
      return request('/members', METHOD.POST(params))
    },
    login(params) {
      return requestWithJsonData('/login', METHOD.POST(params))
    },
    find() {
      return requestWithJsonData('/mypage', METHOD.GET_WITH_AUTH())
    },
    edit(data) {
      return request(`/members/${data.id}`, METHOD.PUT(data))
    }
  }

  return {
    line,
    path,
    member,
  }
})()

export default api
