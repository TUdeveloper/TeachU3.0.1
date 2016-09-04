package com.mai.aso.masaya.teachu.info;

/**
 * Created by MasayaAso on 9/4/16.
 */
public class FbInfo {
    //Firebase上でのキーの名前はここから参照する事
    //Users
    public static final String USERS="users";
    public static final String USERS_GENERAL="general";
        public static final String GEN_FIRST_NAME="first_name";
        public static final String GEN_LAST_NAME="last_name";
        public static final String GEN_EMAIL="email";
        public static final String GEN_GENDER="gender";
        public static final String GEN_BIRTHDAY="birthday";
        public static final String GEN_MOTHER_TONGUE="gender";
        public static final String GEN_LOCATION="location";
        public static final String GEN_SCHOOL="school";
        public static final String GEN_JOB="job";
        public static final String GEN_TEACHER="teacher";
        public static final String GEN_STUDENT="student";

    public static final String USERS_TEACHER="teacher";
        public static final String TEACH_RATE="rate";
        public static final String TEACH_EXPERIENCE="experience";
        public static final String TEACH_TEACH_LANGUAGE="language_to_teach";
            public static final String TEACH_LANGUAGE_1="1";
                public static final String TEACH_LANGUAGE_1_LANGUAGE="language";
                public static final String TEACH_LANGUAGE_1_LEVEL="level";
            public static final String TEACH_LANGUAGE_2="2";
                public static final String TEACH_LANGUAGE_2_LANGUAGE="language";
                public static final String TEACH_LANGUAGE_2_LEVEL="level";
        public static final String TEACH_WHERE="where_to_teach";
            public static final String TEACH_WHERE_1="1";
            public static final String TEACH_WHERE_2="2";
            public static final String TEACH_WHERE_3="3";
        public static final String TEACH_FEE="teaching_fee";
            public static final String FEE_CURRENCY="currency";
            public static final String FEE_FEE="fee";
        public static final String TEACH_FREE_TIME="free_time";
            public static final String TEACH_FREE_MON="mon";
            public static final String TEACH_FREE_TUE="tue";
            public static final String TEACH_FREE_WED="wed";
            public static final String TEACH_FREE_THU="thu";
            public static final String TEACH_FREE_FRI="fri";
            public static final String TEACH_FREE_SAT="sat";
            public static final String TEACH_FREE_SUN="sun";
        public static final String TEACH_PRO="pro";
        public static final String TEACH_KATEI="katei_teacher";
        public static final String TEACH_COMMENT="comment";

    public static final String USERS_STUDENT="student";
        public static final String STUD_LEARN_LANGUAGE="language_to_learn";
            public static final String LEARN_LANGUAGE_1="1";
                public static final String LEARN_LANGUAGE_1_LANGUAGE="language";
                public static final String LEARN_LANGUAGE_1_LEVEL="level";
            public static final String LEARN_LANGUAGE_2="2";
                public static final String LEARN_LANGUAGE_2_LANGUAGE="language";
                public static final String LEARN_LANGUAGE_2_LEVEL="level";
        public static final String LEARN_WHERE="where_to_learn";
            public static final String LEARN_WHERE_1="1";
            public static final String LEARN_WHERE_2="2";
            public static final String LEARN_WHERE_3="3";
        public static final String LEARN_FREE_TIME="free_time";
            public static final String LEARN_FREE_MON="mon";
            public static final String LEARN_FREE_TUE="tue";
            public static final String LEARN_FREE_WED="wed";
            public static final String LEARN_FREE_THU="thu";
            public static final String LEARN_FREE_FRI="fri";
            public static final String LEARN_FREE_SAT="sat";
            public static final String LEARN_FREE_SUN="sun";

    public static final String USERS_FAVORITE="favorite";
    public static final String USERS_CHAT_ROOM="chat_room";
    public static final String USERS_MEETING_CARD="meeting_card";
        public static final String USERS_CARD_NOT_DONE="not_done";
        public static final String USERS_CARD_DONE="done";

    //Meeting Card
    public static final String MEETING_CARD="meeting_card";
        public static final String MEETING_CARD_NOT_DONE="not_done";
            public static final String NOT_DONE_TEACHER="teacher";
            public static final String NOT_DONE_STUDENT="student";
            public static final String NOT_DONE_WHAT="what";
            public static final String NOT_DONE_WHERE="where";
            public static final String NOT_DONE_WHEN="when";
            public static final String NOT_DONE_TIME="time";
            public static final String NOT_DONE_FEE="fee";
        public static final String MEETING_CARD_DONE="done";

    //Chat Room

    public static final String CHILD_USERS="users";
    public static final String CHILD_CHAT="chat";

    public static final String USER_EMAIL="email";
    public static final String USER_FIRST_NAME="first_name";
    public static final String USER_LAST_NAME="last_name";
    public static final String USER_BIRTHDAY="birthday";
    public static final String USER_EDUCATION="education";
    public static final String USER_GENDER="gender";
    public static final String USER_COUNTRY="country";
    public static final String USER_NATIVE_LANGUAGE="native_language";
    public static final String USER_LEARN_LANGUAGE="learn_language";
    public static final String USER_LEARN_LANGUAGE2="learn_language2";
}
