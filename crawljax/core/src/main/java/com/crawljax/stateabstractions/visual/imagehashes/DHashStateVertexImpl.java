package com.crawljax.stateabstractions.visual.imagehashes;

import com.crawljax.core.state.StateVertexImpl;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * The state vertex class which represents a state in the browser. When iterating over the possible
 * candidate elements every time a candidate is returned its removed from the list so it is a one
 * time only access to the candidates.
 */
public class DHashStateVertexImpl extends StateVertexImpl {

	private static final long serialVersionUID = 123400017983489L;

	String dHash;

	/**
	 * Creates a current state without an url and the stripped dom equals the dom.
	 *
	 * @param name the name of the state
	 * @param dom  the current DOM tree of the browser
	 */
	@VisibleForTesting
	public DHashStateVertexImpl(int id, String name, String dom, String dHashVisual) {
		this(id, null, name, dom, dom, dHashVisual);
	}

	/**
	 * Defines a State.
	 *
	 * @param url         the current url of the state
	 * @param name        the name of the state
	 * @param dom         the current DOM tree of the browser
	 * @param strippedDom the stripped dom by the OracleComparators
	 */
	public DHashStateVertexImpl(int id, String url, String name, String dom, String strippedDom,
			String dHashVisual) {
		super(id, url, name, dom, strippedDom);
		this.dHash = dHashVisual;
	}

/**
 * Two states are clones only when both their visual hash and
 * normalized DOM are equal.
 *
 * The visual hash captures the general page layout, while the
 * stripped DOM preserves meaningful differences such as visible
 * text, created entities and form values.
 */
  @Override
  public int hashCode() {
      return Objects.hashCode(
              dHash,
              getStrippedDom()
      );
  }

  @Override
  public boolean equals(Object object) {
      if (this == object) {
          return true;
      }

      if (!(object instanceof DHashStateVertexImpl)) {
          return false;
      }

      DHashStateVertexImpl that =
              (DHashStateVertexImpl) object;

      return Objects.equal(
                  this.dHash,
                  that.getDHashVisual()
              )
              && Objects.equal(
                  this.getStrippedDom(),
                  that.getStrippedDom()
              );
  }

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", super.getId())
				.add("name", super.getName())
				.add("DHASH", dHash).toString();
	}

	public String getDHashVisual() {
		return dHash;
	}

}
