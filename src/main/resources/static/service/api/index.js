const METHOD = {
  GET() {
    return {
      method: "GET",
      headers: {
        "Authorization": localStorage.getItem("Authorization"),
      },
    }
  },
  PUT() {
    return {
      method: 'PUT',
      headers: {
        "Authorization": localStorage.getItem("Authorization"),
      },
    }
  },
  DELETE() {
    return {
      method: 'DELETE',
      headers: {
        "Authorization": localStorage.getItem("Authorization"),
      },
    }
  },
  POST(data) {
    return {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        "Authorization": localStorage.getItem("Authorization"),
      },
      body: JSON.stringify({
        ...data
      }),
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
    join(joinRequest) {
      return request("/members", METHOD.POST(joinRequest));
    },
    login(loginRequest) {
      return request("/oauth/token", METHOD.POST(loginRequest));
    }
  }

  return {
    line,
    path,
    member,
  }
})()

export default api
