package br.com.mauker.lib.freeradiogroup

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class FreeRadioGroup @JvmOverloads constructor(
    mContext: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttributes: Int = 0
): View(mContext, attributeSet, defStyleAttributes) {

    companion object {
        private const val EMPTY_STRING = ""
        private const val ID_SEPARATOR = ","
        private const val INVALID_INDEX = -1
        private const val DEF_TYPE_ID = "id"
    }

    private var referenceIds: String = EMPTY_STRING

    private val ids = mutableListOf<Int>()

    private var checkedId = NO_ID
    // If this is true, all changes on check states will be ignored
    private var lockCheckedChanges = false

    private var onCheckedChangeListener: OnCheckedChangeListener? = null

    private val onChildCheckedChangedListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (lockCheckedChanges) {
                return@OnCheckedChangeListener
            }

            lockCheckedChanges = true

            if (checkedId != NO_ID) {
                setCheckedStateForView(checkedId, false)
            }

            lockCheckedChanges = false

            setCheckedId(buttonView.id)
        }

    private val onAttachListener = object: OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View?) {
            addRadioButtonsListeners(parent as ViewGroup)
            setInitialCheck()
        }

        override fun onViewDetachedFromWindow(v: View?) {
            removeRadioButtonsListeners(parent as ViewGroup)
        }
    }

    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener) {
        onCheckedChangeListener = listener
    }

    fun getCheckedRadioButtonId() = checkedId

    init {
        if (isInEditMode.not()) {
            importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
            isClickable = false
            isFocusable = false

            if (id == NO_ID) {
                id = generateViewId()
            }

            initAttrs(attributeSet)
            addOnAttachStateChangeListener(onAttachListener)
            checkIfNeedsConstraints()
        }
    }

    private fun initAttrs(attributeSet: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.FreeRadioGroup)

        for (i in 0..typedArray.indexCount) {
            val attr = typedArray.getIndex(i)
            if (attr == R.styleable.FreeRadioGroup_referenced_ids) {
                referenceIds = typedArray.getString(i) ?: EMPTY_STRING
                setIds()
            }
        }

        if (typedArray.hasValue(R.styleable.FreeRadioGroup_checkedRadioButton)) {
            checkedId = typedArray.getResourceId(
                R.styleable.FreeRadioGroup_checkedRadioButton,
                NO_ID
            )
        }

        typedArray.recycle()

        if (isInEditMode) {
            visibility = GONE
        }
    }

    private fun setIds() {
        if (referenceIds.isBlank()) {
            return
        }

        var begin = 0
        while (true) {
            val end = referenceIds.indexOf(ID_SEPARATOR, begin)

            if (end == INVALID_INDEX) {
                addId(referenceIds.substring(begin))
                break
            }

            addId(referenceIds.substring(begin, end))
            begin = end + 1
        }
    }

    private fun addId(idStr: String) {
        if (idStr.isBlank()) {
            return
        }
        if (context == null) {
            return
        }

        val idStrTrimmed = idStr.trim()
        var tag = 0
        try {
            val res = R.id::class.java
            val field = res.getField(idStrTrimmed)
            tag = field.getInt(null)
        } catch (e: Exception) {
            // Do nothing
        }

        if (tag == 0) {
            tag = context.run {
                resources.getIdentifier(
                    idStrTrimmed,
                    DEF_TYPE_ID,
                    packageName
                )
            }
        }
        if (tag == 0 && isInEditMode && parent is ConstraintLayout) {
            val cl = parent as? ConstraintLayout
            cl?.let {
                val value = it.getDesignInformation(0, idStrTrimmed)
                if (value != null && value is Int) {
                    tag = value
                }
            }
        }

        if (tag != 0) {
            ids.add(tag)
        } else {
            Log.w("FreeRadioGroup", "Could not find id of \"$idStrTrimmed\"")
        }
    }

    private fun addRadioButtonsListeners(container: ViewGroup) {
        ids.forEach {
            val view: View = container.findViewById(it)

            if (view is RadioButton) {
                view.setOnCheckedChangeListener(onChildCheckedChangedListener)
            }
        }
    }

    private fun removeRadioButtonsListeners(container: ViewGroup) {
        ids.forEach {
            val view: View = container.findViewById(it)

            if (view is RadioButton) {
                view.setOnCheckedChangeListener(null)
            }
        }
    }

    private fun setCheckedStateForView(viewId: Int, isChecked: Boolean) {
        val parentContainer = parent

        if (parentContainer is ViewGroup) {
            val view: View? = parentContainer.findViewById(viewId)

            if (view != null && view is RadioButton) {
                view.isChecked = isChecked
            }
        }
    }

    private fun setCheckedId(id: Int) {
        val changed = id != checkedId
        checkedId = id

        onCheckedChangeListener?.onCheckedChanged(this, id)

        if (changed) {
            // TODO - notify autofill?
        }
    }

    private fun setInitialCheck() {
        if (checkedId != NO_ID) {
            lockCheckedChanges = true
            setCheckedStateForView(checkedId, true)
            lockCheckedChanges = false
            setCheckedId(checkedId)
        }
    }

    private fun checkIfNeedsConstraints() {
        val container = parent

        if (container is ConstraintLayout) {
            val cs = ConstraintSet()
            cs.clone(container)

            cs.connect(id, ConstraintSet.TOP, container.id, ConstraintSet.TOP)
            cs.connect(id, ConstraintSet.BOTTOM, container.id, ConstraintSet.BOTTOM)
            cs.connect(id, ConstraintSet.LEFT, container.id, ConstraintSet.LEFT)
            cs.connect(id, ConstraintSet.RIGHT, container.id, ConstraintSet.RIGHT)

            cs.applyTo(container)
        }
    }

    fun check(id: Int) {
        if (id != NO_ID && (id == checkedId)) {
            return
        }

        if (checkedId != NO_ID) {
            setCheckedStateForView(checkedId, false)
        }

        if (id != NO_ID) {
            setCheckedStateForView(id, true)
        }

        setCheckedId(id)
    }

    fun clearCheck() {
        check(NO_ID)
    }

    // region View super methods

    override fun onDraw(canvas: Canvas?) {
        // Just don't.
    }

//    override fun setVisibility(visibility: Int) {
//        // Don't.
//    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(0,0)
    }



    //endregion
}