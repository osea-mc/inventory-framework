package me.devnatan.inventoryframework.component;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import me.devnatan.inventoryframework.Ref;
import me.devnatan.inventoryframework.VirtualView;
import me.devnatan.inventoryframework.context.IFComponentRenderContext;
import me.devnatan.inventoryframework.context.IFComponentUpdateContext;
import me.devnatan.inventoryframework.context.IFContext;
import me.devnatan.inventoryframework.context.IFSlotClickContext;
import me.devnatan.inventoryframework.state.State;

public abstract class PlatformComponent extends AbstractComponent implements Component {

    private final boolean cancelOnClick;
    private final boolean closeOnClick;
    private final boolean updateOnClick;
    private final Consumer<? super IFComponentRenderContext> renderHandler;
    private final Consumer<? super IFComponentUpdateContext> updateHandler;
    private final Consumer<? super IFSlotClickContext> clickHandler;

    protected PlatformComponent(
            String key,
            VirtualView root,
            Ref<Component> reference,
            Set<State<?>> watchingStates,
            Predicate<? extends IFContext> displayCondition,
            Consumer<? super IFComponentRenderContext> renderHandler,
            Consumer<? super IFComponentUpdateContext> updateHandler,
            Consumer<? super IFSlotClickContext> clickHandler,
            boolean cancelOnClick,
            boolean closeOnClick,
            boolean updateOnClick) {
        super(key, root, reference, watchingStates, displayCondition);
        this.renderHandler = renderHandler;
        this.updateHandler = updateHandler;
        this.clickHandler = clickHandler;
        this.cancelOnClick = cancelOnClick;
        this.closeOnClick = closeOnClick;
        this.updateOnClick = updateOnClick;
    }

    // region Public Builder Methods
    public final boolean isCancelOnClick() {
        return cancelOnClick;
    }

    public final boolean isCloseOnClick() {
        return closeOnClick;
    }

    public final boolean isUpdateOnClick() {
        return updateOnClick;
    }
    // endregion

    // region Internal Components API
    public final Consumer<? super IFComponentRenderContext> getRenderHandler() {
        return renderHandler;
    }

    public final Consumer<? super IFComponentUpdateContext> getUpdateHandler() {
        return updateHandler;
    }

    public final Consumer<? super IFSlotClickContext> getClickHandler() {
        return clickHandler;
    }
    // endregion
}
