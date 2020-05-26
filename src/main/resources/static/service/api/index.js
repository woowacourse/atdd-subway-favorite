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
  },
  DELETE_WITH_JSON(data) {
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
  }
};

const api = (() => {
  const request = (uri, config) => fetch(uri, config).then(response => {
    if (!response.ok) {
      response.json().then(response => alert(response.errorMessage))
      return;
    }
  });

  const requestWithJsonData = (uri, config) =>
    fetch(uri, config).then(response => {
      if (!response.ok) {
        response.json().then(response => alert(response.errorMessage))
        return;
      }
      return response.json().catch(() => {
        throw new Error("json fail");
      });
    });

  const requestWithNoContent = (uri, config) => fetch(uri, config).then(response => {
    if (!response.ok) {
      response.json().then(response => alert(response.errorMessage))
      return;
    }
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
      return request(`/me/bearer`, METHOD.PUT(updatedInfo));
    },
    delete() {
      return request(`/me/bearer`, METHOD.DELETE());
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
    delete(body) {
      return requestWithNoContent(`/favorites`, METHOD.DELETE_WITH_JSON(body));
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
