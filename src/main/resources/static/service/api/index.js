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
  const request = (uri, config) => fetch(uri, config).then(data => {
    if (!data.ok) {
      return data.json().then(error => {
        throw new Error(error.errorMessage)
      })
    }
  })
  const requestWithJsonData = (uri, config) => fetch(uri, config).then(data => {
    if (data.ok) {
      return data.json()
    }
    return data.json().then(error => {
      throw new Error(error.errorMessage)
    })
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
      join(params) {
          return request(`/members`, METHOD.POST(params))
      },
      login(params) {
          return requestWithJsonData(`/oauth/token`, METHOD.POST(params))
      }
  }

  return {
    line,
    path,
    member
  }
})()

export default api
