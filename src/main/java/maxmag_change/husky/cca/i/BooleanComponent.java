package maxmag_change.husky.cca.i;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface BooleanComponent extends Component {
    boolean getValue();
    void setValue(boolean bl);
    void syncValue(Object object, boolean bl);
}
