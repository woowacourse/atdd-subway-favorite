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
      return requestWithJsonData(`/oauth/token`, METHOD.POST(loginInfo));
    }
  };

  const loginMember = {
    get() {
      return requestWithJsonData(`/me/bearer`, METHOD.GET_WITH_AUTH());
    },
    update(updatedInfo) {
      return request(`/me`, METHOD.PUT(updatedInfo));
    },
    delete() {
      return request(`/me`, METHOD.DELETE());
    }
  };

  return {
    member,
    loginMember
  };
})();

export default api;
