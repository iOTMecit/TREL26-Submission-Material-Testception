package com.crawljax.stateabstractions.visual.imagehashes;

import com.crawljax.core.state.StateVertex;
import com.crawljax.core.state.StateVertexImpl;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.opencv.core.Mat;

/**
 * The state vertex class which represents a state in the browser. When iterating over the possible
 * candidate elements every time a candidate is returned its removed from the list so it is a one
 * time only access to the candidates.
 */
public class PerceptualImageHashStateVertexImpl extends StateVertexImpl {

	private static final long serialVersionUID = 123400017983489L;

	PerceptualImageHash hash;

	public Mat hashMat;

	/**
	 * Creates a current state without an url and the @strippedDom same as the @dom.
	 *
	 * @param name the name of the state
	 * @param dom  the current DOM tree of the browser
	 */
	@VisibleForTesting PerceptualImageHashStateVertexImpl(int id, String name, String dom, PerceptualImageHash visHash,
			Mat hashMat) {
		this(id, null, name, dom, dom, visHash, hashMat);
	}

	/**
	 * Defines a State.
	 *
	 * @param url         the current url of the state
	 * @param name        the name of the state
	 * @param dom         the current DOM tree of the browser
	 * @param strippedDom the stripped dom by the OracleComparators
	 */
	public PerceptualImageHashStateVertexImpl(int id, String url, String name, String dom, String strippedDom,
			PerceptualImageHash visHash,
			Mat hashMat) {
		super(id, url, name, dom, strippedDom);
		this.hash = visHash;
		this.hashMat = hashMat;
	}

/**
 * Two states are exact clones only when:
 *
 * 1. Their perceptual image hashes are considered visually equivalent.
 * 2. Their normalized/stripped DOMs are identical.
 *
 * Visual distance is still used for near-duplicate calculations through
 * getDist() and inThreshold().
 */
  @Override
  public int hashCode() {
      /*
       * The visual comparison is threshold based. Two different hash matrices
       * may therefore be considered equal. Using hashMat here could violate
       * the equals/hashCode contract.
       *
       * Since equals() also requires equal stripped DOMs, stripped DOM alone
       * is a safe hash-code source.
       */
      return Objects.hashCode(getStrippedDom());
  }

  @Override
  public boolean equals(Object object) {
      if (this == object) {
          return true;
      }

      if (!(object instanceof PerceptualImageHashStateVertexImpl)) {
          return false;
      }

      PerceptualImageHashStateVertexImpl that =
              (PerceptualImageHashStateVertexImpl) object;

      double distance =
              hash.compare(this.hashMat, that.hashMat);

      boolean sameVisualState =
              distance == 0.0
              || (distance >= hash.minThreshold
                  && distance <= hash.maxThreshold);

      boolean sameStrippedDom =
              Objects.equal(
                      this.getStrippedDom(),
                      that.getStrippedDom()
              );

      return sameVisualState && sameStrippedDom;
  }

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", super.getId())
				.add("name", super.getName())
				.add("hash", hash).toString();
	}

	@Override
	public boolean inThreshold(StateVertex vertexOfGraph) {
		// Only implemented when there is a threshold for near duplicates
		if (vertexOfGraph instanceof PerceptualImageHashStateVertexImpl) {
			PerceptualImageHashStateVertexImpl vertex = (PerceptualImageHashStateVertexImpl) vertexOfGraph;
			double distance = hash.compare(this.hashMat, vertex.hashMat);
			return distance >= hash.minThreshold && distance <= hash.maxThreshold;
		}
		return false;
	}

	@Override
	public double getDist(StateVertex vertexOfGraph) {
		if (vertexOfGraph instanceof PerceptualImageHashStateVertexImpl) {
			PerceptualImageHashStateVertexImpl vertex = (PerceptualImageHashStateVertexImpl) vertexOfGraph;
			return hash.compare(this.hashMat, vertex.hashMat);
		}
		return -1;
	}
}
