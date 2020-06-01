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
  },

  AUTH_GET(token) {
    return {
      method: 'GET',
      headers: {
        'authorization': 'Bearer ' + token,
        'Content-Type': 'application/json'
      }
    }
  },
  //TODO: AUTH를 지우든가 해서 처리
  AUTH_POST(token, data) {
    return {
      method: 'POST',
      headers: {
        'authorization' : 'Bearer ' + token,
        'Content-Type' : 'application/json'
      },
      body: JSON.stringify({
        ...data
      })
    }
  },

  AUTH_PUT(token, data) {
    return {
      method: 'PUT',
      headers: {
        'authorization': 'Bearer ' + token,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        ...data
      })
    }
  },
  AUTH_DELETE(token, data) {
    return {
      method: 'DELETE',
      headers: {
        'authorization': 'Bearer ' + token,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        ...data
      })
    }
  },
};

const api = (() => {
  const request = (uri, config) => fetch(uri, config).then(data => {
    if (data.status === 401) {
      window.location.href = "/login";
    }

    if (!data.ok) {
      throw new Error();
    }

    return data;
  });
  const requestWithJsonData = (uri, config) => fetch(uri, config).then(data => {
    if (data.status === 401) {
      window.location.href = "/login";
      return;
    }
    if (!data.ok) {
      throw new Error();
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
  };

  const path = {
    find(params) {
      return requestWithJsonData(`/paths?source=${params.source}&target=${params.target}&type=${params.type}`)
    }
  };

  const user = {
    join(params) {
      return request(`/join`, METHOD.POST(params))
    },
    login(params) {
      return requestWithJsonData(`/oauth/token`, METHOD.POST(params))
    },
    getInfo(token) {
      return requestWithJsonData(`/me/bearer`, METHOD.AUTH_GET(token))
    },
    update(token, data) {
      return request(`/members`, METHOD.AUTH_PUT(token, data))
    },
    delete(token) {
      return request(`/members/`, METHOD.AUTH_DELETE(token))
    }
  };

  const favorite = {
    register(token, data) {
      return request(`/favorite-paths`, METHOD.AUTH_POST(token, data))
    },
    unregister(token, data) {
      return request(`/favorite-paths`, METHOD.AUTH_DELETE(token, data))
    },
    findAll(token) {
      return requestWithJsonData(`/favorite-paths`, METHOD.AUTH_GET(token))
    }
  };

  return {
    line,
    path,
    user,
    favorite
  }
})();

export default api
