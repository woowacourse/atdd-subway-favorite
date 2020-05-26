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
  DELETE() {
    return {
      method: "DELETE",
      headers: {
        Authorization: localStorage.getItem("jwt") || ""
      }
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

const api = (() => {
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
      return requestWithJsonData(`/login`, METHOD.POST(loginInfo));
    }
  };

  const loginMember = {
    get() {
      return requestWithJsonData(`/me`, METHOD.GET_WITH_AUTH());
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
    create(favoritePath) {
      return request(`/favorites`, METHOD.POST(favoritePath));
    },
    get(id) {
      return requestWithJsonData(`/favorites/${id}`);
    },
    getAll() {
      return requestWithJsonData(`/favorites`, METHOD.GET_WITH_AUTH());
    },
    delete(id) {
      return request(`/favorites/${id}`, METHOD.DELETE());
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
