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
  POST_WITH_TOKEN(data) {
    return {
      method: 'POST',
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
  DELETE() {
    return {
      method: 'DELETE'
    }
  },
  DELETE_WITH_TOKEN() {
    return {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': localStorage.getItem("tokenType") + " " + localStorage.getItem("accessToken")
      }
    }
  }
}

const api = (() => {
  const request = (uri, config) => fetch(uri, config).then(async data => {
    if (!data.ok) {
      throw new Error(await data.text())
    }
    return data
  });

  const requestWithJsonData = (uri, config) => fetch(uri, config).then(async data => {
    if (!data.ok) {
      throw new Error(await data.text())
    }
    return data.json()
  });

  const line = {
    getAll() {
      return request(`/lines/detail`)
    },
    getAllDetail() {
      return requestWithJsonData(`/lines/detail`)
    }
  }

  const station = {
    get(id) {
      return requestWithJsonData(`/stations/${id}`)
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
    },
    deleteMyInformation() {
      return request(`/me`, METHOD.DELETE_WITH_TOKEN())
    },
    getMyFavorites() {
      return requestWithJsonData(`/me/favorites`, METHOD.GET_WITH_TOKEN())
    },
    addFavorite(params) {
      return request(`/me/favorites`, METHOD.POST_WITH_TOKEN(params))
    },
    deleteFavorite(favoriteId) {
      return request(`/me/favorites/${favoriteId}`, METHOD.DELETE_WITH_TOKEN())
    }
  }

  return {
    line,
    station,
    path,
    memberWithoutToken,
    memberWithToken
  }
})()

export default api
