package productions.darthplagueis.contentvault.scrollingphotos;

import java.util.List;

import productions.darthplagueis.contentvault.data.UserContent;

public interface ScrollingContentNavigator {

    void onContentRetrieved(List<UserContent> userContents);

    void onSetViewPagerItem(int position);
}
