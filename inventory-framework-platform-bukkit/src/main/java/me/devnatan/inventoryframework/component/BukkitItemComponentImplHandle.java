package me.devnatan.inventoryframework.component;

import me.devnatan.inventoryframework.InventoryFrameworkException;
import me.devnatan.inventoryframework.context.ComponentRenderContext;
import me.devnatan.inventoryframework.context.IFComponentContext;
import me.devnatan.inventoryframework.context.IFComponentUpdateContext;
import me.devnatan.inventoryframework.context.IFRenderContext;
import me.devnatan.inventoryframework.context.IFSlotClickContext;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class BukkitItemComponentImplHandle extends BukkitComponentHandle<BukkitItemComponentBuilder<Void>> {

    @Override
    public void rendered(@NotNull ComponentRenderContext context) {
        final BukkitItemComponentImpl component = (BukkitItemComponentImpl) context.getComponent();

        if (component.getRenderHandler() != null) {
            final int initialSlot = component.getPosition();
            component.getRenderHandler().accept(context);

            // Externally managed components have its own displacement measures
            // FIXME Missing implementation
            // TODO Component-based context do not need displacement measures?
            if (!component.isManagedExternally()) {
                final int updatedSlot = ((BukkitItemComponentImpl) context.getComponent()).getPosition();
                component.setPosition(updatedSlot);

                if (updatedSlot == -1 && initialSlot == -1) {
                    // TODO needs more user-friendly "do something"-like message
                    throw new InventoryFrameworkException("Missing position (unset slot) for item component");
                }

                // TODO Misplaced - move this to overall item component misplacement check
                if (initialSlot != -1 && initialSlot != updatedSlot) {
                    context.getContainer().removeItem(initialSlot);
                    component.hide();
                }
            }

            // context.getContainer().renderItem(getPosition(), context.getResult());
            component.setVisible(true);
            return;
        }

        if (component.getItemStack() == null) {
            if (context.getContainer().getType().isResultSlot(component.getPosition())) {
                component.show();
                return;
            }
            throw new IllegalStateException("At least one fallback item or render handler must be provided");
        }

        context.getContainer().renderItem(component.getPosition(), component.getItemStack());
        component.show();
    }

    @Override
    public void updated(@NotNull IFComponentUpdateContext context) {
        if (context.isCancelled()) return;

        final PlatformComponent component = (PlatformComponent) context.getComponent();

        // Static item with no `displayIf` must not even reach the update handler
        if (!context.isForceUpdate() && component.getDisplayCondition() == null && component.getRenderHandler() == null)
            return;

        if (component.isVisible() && component.getUpdateHandler() != null) {
            component.getUpdateHandler().accept(context);
            if (context.isCancelled()) return;
        }

        ((IFRenderContext) context.getTopLevelContext()).renderComponent(component);
    }

    @Override
    public void cleared(@NotNull IFComponentContext context) {
        final Component component = context.getComponent();
        component.getContainer().removeItem(((ItemComponent) component).getPosition());
    }

    @Override
    public void clicked(@NotNull IFSlotClickContext context) {
        final PlatformComponent component = (PlatformComponent) context.getComponent();
        if (component.isUpdateOnClick()) context.update();
    }

    @Override
    public BukkitItemComponentBuilder<Void> builder() {
        return new BukkitItemComponentBuilder<>();
    }
}
