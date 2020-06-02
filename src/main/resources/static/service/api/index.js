const METHOD = {
  PUT(data) {
    return {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
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
    join(joinForm) {
      return request(`/members`, METHOD.POST(joinForm));
    },
    login(loginForm) {
      return request(`/login`, METHOD.POST(loginForm));
    },
    myPage(){
      return requestWithJsonData(`/me`);
    },
    update(id, updateForm){
      return request(`/members/`+id, METHOD.PUT(updateForm));
    },
    delete(id){
      return request(`/members/`+id, METHOD.DELETE());
    }
  }

  const favorite = {
    create(favoritePath) {
      return request(`/favorites`, METHOD.POST(favoritePath));
    },
    get(id) {
      return requestWithJsonData(`/favorites/${id}`);
    },
    getAll() {
      return requestWithJsonData(`/me/favorites`);
    },
    delete(id) {
      return request(`/favorites/${id}`, METHOD.DELETE());
    }
  };

  return {
    line,
    path,
    member,
    favorite,
  }
})()

export default api
