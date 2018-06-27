package productions.darthplagueis.contentvault.data.source.content;

import java.util.List;

import productions.darthplagueis.contentvault.data.UserContent;

public interface UserContentCallBack {

    interface GetUserContentCallBack {
        void onContentListRetrieved (List<UserContent> userContents);
    }
}
