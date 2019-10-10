package com.ggcode.devknife.ui.base.adapter;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: zbb 33775
 * @date: 2019/4/15 17:19
 * @desc:
 */
public abstract class DKAdapter<T, K extends DKViewHolder> extends RecyclerView.Adapter<K> {

    protected Context mContext;
    protected List<T> mData;
    protected LayoutInflater mLayoutInflater;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnItemChildClickListener mOnItemChildClickListener;
    private OnItemChildLongClickListener mOnItemChildLongClickListener;
    private RecyclerView mRecyclerView;

    public DKAdapter() {
        this(new ArrayList<T>());
    }

    public DKAdapter(List<T> data) {
        mData = new ArrayList<>(data.size());
        mData.addAll(data);
    }

    @Override
    public final K onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        if (mLayoutInflater == null) {
            this.mLayoutInflater = LayoutInflater.from(mContext);
        }
        View view = createView(mLayoutInflater, parent, viewType);
        K baseViewHolder = createBaseViewHolder(view);
        bindViewClickListener(baseViewHolder);
        return baseViewHolder;
    }

    private void bindViewClickListener(final DKViewHolder viewHolder) {
        if (viewHolder == null) {
            return;
        }
        final View view = viewHolder.itemView;
        if (view == null) {
            return;
        }
        if (getOnItemClickListener() != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getOnItemClickListener().onItemClick(DKAdapter.this, v, viewHolder.getLayoutPosition());
                }
            });
        }
        if (getOnItemLongClickListener() != null) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return getOnItemLongClickListener()
                            .onItemLongClick(DKAdapter.this, v, viewHolder.getLayoutPosition());
                }
            });
        }
    }

    protected abstract View createView(LayoutInflater inflater, ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull K holder, int position) {
        convert(holder, getItem(position));
    }

    protected abstract void convert(K helper, T item);

    @Nullable
    public T getItem(@IntRange(from = 0) int position) {
        if (position < mData.size()) {
            return mData.get(position);
        }
        return null;
    }

    public void bindToRecyclerView(RecyclerView recyclerView) {
        if (mRecyclerView != null) {
            throw new RuntimeException("Don't bind twice");
        }
        mRecyclerView = recyclerView;
        mRecyclerView.setAdapter(this);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public final OnItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    public final OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Nullable
    public final OnItemChildClickListener getOnItemChildClickListener() {
        return mOnItemChildClickListener;
    }

    public void setOnItemChildClickListener(OnItemChildClickListener listener) {
        mOnItemChildClickListener = listener;
    }

    @Nullable
    public final OnItemChildLongClickListener getOnItemChildLongClickListener() {
        return mOnItemChildLongClickListener;
    }

    public void setOnItemChildLongClickListener(OnItemChildLongClickListener listener) {
        mOnItemChildLongClickListener = listener;
    }

    protected K createBaseViewHolder(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        K k;
        // 泛型擦除会导致z为null
        if (z == null) {
            k = (K) new DKViewHolder(view);
        } else {
            k = createGenericKInstance(z, view);
        }
        return k != null ? k : (K) new DKViewHolder(view);
    }

    @SuppressWarnings("unchecked")
    private K createGenericKInstance(Class z, View view) {
        try {
            Constructor constructor;
            // inner and unstatic class
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                constructor.setAccessible(true);
                return (K) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (K) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Class getInstancedGenericKClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (DKViewHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                } else if (temp instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) temp).getRawType();
                    if (rawType instanceof Class && DKViewHolder.class.isAssignableFrom((Class<?>) rawType)) {
                        return (Class<?>) rawType;
                    }
                }
            }
        }
        return null;
    }

    public void addData(@NonNull Collection<? extends T> newData) {
        mData.addAll(newData);
        notifyItemRangeInserted(mData.size() - newData.size(), newData.size());
    }

    public void addData(@NonNull T data) {
        mData.add(data);
        notifyItemInserted(mData.size() - 1);
    }

    public void remove(@IntRange(from = 0) int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size() - position);
    }

    public void setData(@IntRange(from = 0) int index, @NonNull T data) {
        mData.set(index, data);
        notifyItemChanged(index);
    }

    public void setNewData(@NonNull List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void replaceData(@NonNull Collection<? extends T> data) {
        if (data != mData) {
            mData.clear();
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mData;
    }

    public Context getContext() {
        return mContext;
    }

    public interface OnItemChildClickListener {

        void onItemChildClick(DKAdapter adapter, View view, int position);
    }

    public interface OnItemChildLongClickListener {

        boolean onItemChildLongClick(DKAdapter adapter, View view, int position);
    }

    public interface OnItemLongClickListener {

        boolean onItemLongClick(DKAdapter adapter, View view, int position);
    }

    public interface OnItemClickListener {

        void onItemClick(DKAdapter adapter, View view, int position);
    }
}
