const METHOD = {
  GET_WITH_AUTH() {
    return {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: localStorage.getItem("jwt") || ""
      }
    };
  },
  PUT(data) {
    return {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: localStorage.getItem("jwt") || ""
      },
      body: JSON.stringify({
        ...data
      })
    };
  },
  DELETE(data) {
    return {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: localStorage.getItem("jwt") || ""
      },
      body: JSON.stringify({
        ...data
      })
    };
  },
  POST(data) {
    return {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: localStorage.getItem("jwt") || ""
      },
      body: JSON.stringify({
        ...data
      })
    };
  }
};

const resolver = (response) => {
  return new Promise((resolve, reject) => {
    let func;
    response.status < 400 ? func = resolve : func = reject;
    if (response.status !== 204) {
      response.json().then(data => func({'status': response.status, 'body': data}));
      return;
    }
    func({'status': response.status})
  });
}

const api = (() => {
  const customRequest = (uri, config) => fetch(uri, config).then(resolver);
  const request = (uri, config) => fetch(uri, config);
  const requestWithJsonData = (uri, config) =>
      fetch(uri, config).then(response => {
        if (!response.ok) {
          return;
        }
        return response.json();
      });

  const member = {
    get(id) {
      return requestWithJsonData(`/members/${id}`);
    },
    create(newMember) {
      return request(`/members`, METHOD.POST(newMember));
    },
    update(id, updatedData) {
      return request(`/members/${id}`, METHOD.PUT(updatedData));
    },
    delete(id) {
      return request(`/members/${id}`, METHOD.DELETE());
    },
    login(loginInfo) {
      return customRequest(`/login`, METHOD.POST(loginInfo));
    }
  };

  const loginMember = {
    get() {
      return customRequest(`/me`, METHOD.GET_WITH_AUTH());
    },
    update(updatedInfo) {
      return request(`/me`, METHOD.PUT(updatedInfo));
    },
    delete() {
      return request(`/me`, METHOD.DELETE());
    }
  };

  const line = {
    getAll() {
      return request(`/lines/detail`);
    },
    getAllDetail() {
      return requestWithJsonData(`/lines/detail`);
    }
  };

  const path = {
    find(params) {
      return requestWithJsonData(
          `/paths?source=${params.source}&target=${params.target}&type=${params.type}`
      );
    }
  };

  const favorite = {
    create(data) {
      return request(`/me/favorites`, METHOD.POST(data));
    },
    get(id) {
      return requestWithJsonData(`/favorites/${id}`);
    },
    getAll() {
      return customRequest(`/me/favorites`, METHOD.GET_WITH_AUTH());
    },
    delete(data) {
      return request(`/me/favorites`, METHOD.DELETE(data));
    }
  };

  return {
    member,
    loginMember,
    line,
    path,
    favorite
  };
})();

export default api;
