package productions.darthplagueis.contentvault.scrollingphotos;

import java.util.List;

import productions.darthplagueis.contentvault.data.UserContent;

public interface ScrollingContentNavigator {

    void onContentScroll(List<UserContent> userContents);
}
