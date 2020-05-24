const METHOD = {
  GET_WITH_TOKEN() {
    return {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem("tokenType") + " " + localStorage.getItem("accessToken")
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
  },
  PUT_WITH_TOKEN(data) {
    return {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem("tokenType") + " " + localStorage.getItem("accessToken")
      },
      body: JSON.stringify({
        ...data
      })
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

  const memberWithoutToken = {
    create(params) {
      return request(`/members`, METHOD.POST(params))
    },
    login(params) {
      return requestWithJsonData(`/oauth/token`, METHOD.POST(params))
    }
  }

  const memberWithToken = {
    getMyInformation() {
      return requestWithJsonData(`/me`, METHOD.GET_WITH_TOKEN())
    },
    updateMyInformation(params) {
      return request(`/me`, METHOD.PUT_WITH_TOKEN(params))
    }
  }

  return {
    line,
    path,
    memberWithoutToken,
    memberWithToken
  }
})()

export default api
