const memberBaseUrl = "/members";
const myInfoBaseUrl = `${memberBaseUrl}/my-info`;
const myFavoriteBaseUrl = `${myInfoBaseUrl}/favorites`;

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
  const requestWithRedirect = (uri, config) => requestWithThrowError(uri, config)
  .then(response => redirectPage(response));
  const requestWithJsonData = (uri, config) => requestWithThrowError(uri, config)
  .then(response => response.json())
  const requestWithJsonDataAndRedirect = (uri, config) => requestWithThrowError(uri, config)
  .then(response => redirectPage(response))
  .then(response => response.json())

  const requestWithThrowError = (uri, config) => fetch(uri, config)
  .then(response => throwErrorIfStatusIsNotOk(response))

  const throwErrorIfStatusIsNotOk = async response => {
    if (!response.ok) {
      const errorResponse = await response.json();
      throw Error(errorResponse.errorMessage);
    }
    return response;
  }

  const redirectPage = response => {
    if (response.redirected) {
      localStorage.removeItem("jwt")
      location.href = response.url;
    }
    return response;
  }

  const member = {
    create(newMember) {
      return requestWithRedirect(`${memberBaseUrl}`, METHOD.POST(newMember));
    },
    login(loginInfo) {
      return requestWithJsonData(`/oauth/token`, METHOD.POST(loginInfo));
    }
  };

  const loginMember = {
    get() {
      return requestWithJsonDataAndRedirect(`${myInfoBaseUrl}`, METHOD.GET_WITH_AUTH());
    },
    update(updatedInfo) {
      return requestWithRedirect(`${myInfoBaseUrl}`, METHOD.PUT(updatedInfo));
    },
    delete() {
      return requestWithRedirect(`${myInfoBaseUrl}`, METHOD.DELETE());
    }
  };

  const line = {
    getAllDetail() {
      return requestWithJsonDataAndRedirect(`/lines/detail`);
    }
  };

  const path = {
    find(params) {
      return requestWithJsonDataAndRedirect(
        `/paths?source=${params.source}&target=${params.target}&type=${params.type}`
      );
    }
  };

  const favorite = {
    create(favoritePath) {
      return requestWithRedirect(`${myFavoriteBaseUrl}`, METHOD.POST(favoritePath));
    },
    isToggled(sourceId, targetId) {
      return requestWithJsonDataAndRedirect(`${myFavoriteBaseUrl}/existsPath?sourceId=${sourceId}&targetId=${targetId}`,
        METHOD.GET_WITH_AUTH());
    },
    getAll() {
      return requestWithJsonDataAndRedirect(`${myFavoriteBaseUrl}`, METHOD.GET_WITH_AUTH());
    },
    delete(sourceId, targetId) {
      return requestWithRedirect(`${myFavoriteBaseUrl}/source/${sourceId}/target/${targetId}`,
        METHOD.DELETE());
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
