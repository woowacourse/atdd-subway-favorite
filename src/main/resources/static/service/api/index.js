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
      method: 'DELETE',
      headers: {
        "Content-Type": "application/json",
        Authorization: localStorage.getItem("auth") || ""
      }
    }
  },
  POST(data) {
    return {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: localStorage.getItem("auth") || ""
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
      return request('/members/login', METHOD.POST(params))
    },
    find(id) {
      return requestWithJsonData(`/members/${id}`, METHOD.GET_WITH_AUTH())
    },
    edit(data) {
      return request(`/members/${data.id}`, METHOD.PUT(data))
    },
    delete({id}) {
      return request(`/members/${id}`, METHOD.DELETE())
    }
  }

  const favorite = {
    create(id, addFavoriteRequest) {
      return request(`/members/${id}/favorites`, METHOD.POST(addFavoriteRequest));
    },
    get(id) {
      return requestWithJsonData(`/members/${id}/favorites`, METHOD.GET_WITH_AUTH());
    },
    delete(memberId, sourceId, targetId) {
      return request(`/members/${memberId}/favorites/source/${sourceId}/target/${targetId}`, METHOD.DELETE());
    }
  };

  return {
    line,
    path,
    member,
    favorite
  }
})()

export default api
