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
  AUTH_DELETE(token) {
    return {
      method: 'DELETE',
      headers: {
        'authorization': 'Bearer ' + token,
        'Content-Type': 'application/json'
      }
    }
  }
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
    update(token, id, data) {
      return request(`/members/` + id, METHOD.AUTH_PUT(token, data))
    },
    delete(token, id) {
      return request(`/members/` + id, METHOD.AUTH_DELETE(token))
    }
  };

  return {
    line,
    path,
    user
  }
})();

export default api
