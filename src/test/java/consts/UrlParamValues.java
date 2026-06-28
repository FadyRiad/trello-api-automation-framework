package consts;

import base.ConfigLoader;
import java.util.Map;

public class UrlParamValues {

    public static final String VALID_KEY = ConfigLoader.getApiKey();
    public static final String VALID_TOKEN = ConfigLoader.getApiToken();

    public static final Map<String, String> AUTH_QUERY_PARAMS = Map.of(
            "key", VALID_KEY,
            "token", VALID_TOKEN
    );

    public static final String USER_NAME = "fadyriad4";
    public static final String EXISTING_BOARD_ID = "6a2ddc8074cebf3f62872be2";
    public static final String BOARD_ID_TO_UPDATE = "6a2ddebef77e01bdc427b398";
    public static final String EXISTING_LIST_ID = "6a2ddc8174cebf3f62872bfd";
    public static final String EXISTING_CARD_ID = "6a33e2329970ac8cfed11ded";
    public static final String CARD_ID_TO_UPDATE = "6a33e235beafddaa2d6f8e70";
}