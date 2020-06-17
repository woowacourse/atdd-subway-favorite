const METHOD = {
  GET() {
    return {
      method: "GET",
      headers: {
        "Authorization": localStorage.getItem("Authorization"),
      },
    }
  },
  PUT(data) {
    return {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        "Authorization": localStorage.getItem("Authorization"),
      },
      body: JSON.stringify({
        ...data
      }),
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
      return request(`/paths?source=${params.source}&target=${params.target}&type=${params.type}`)
    }
  }

  const me = {
    find() {
      return request(`/me`, METHOD.GET())
    },
    update(updateMemberRequest) {
      return request(`/me`, METHOD.PUT(updateMemberRequest))
    },
    delete() {
      return request(`/me`, METHOD.DELETE())
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

  const favorite = {
    add(addFavoriteRequest) {
      return request("/me/favorites", METHOD.POST(addFavoriteRequest))
    },
    remove(removeFavoriteRequest) {
      return request(`/me/favorites/source/${removeFavoriteRequest.source}/source/${removeFavoriteRequest.target}`,
        METHOD.DELETE());
    },
    removeById(favoriteId) {
      return request(`/me/favorites/${favoriteId}`, METHOD.DELETE());
    },
    get(source, target) {
      return request(`/me/favorites/source/${source}/target/${target}`, METHOD.GET());
    },
    getAll() {
      return requestWithJsonData("/me/favorites", METHOD.GET());
    }
  }

  return {
    line,
    path,
    member,
    me,
    favorite,
  }
})()

export default api
