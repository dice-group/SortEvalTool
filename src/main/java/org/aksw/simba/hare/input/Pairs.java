package org.aksw.simba.hare.input;

public class Pairs {
	public String resultFrom;

	public String getResultFrom() {
		return resultFrom;
	}

	public void setResultFrom(String resultFrom) {
		this.resultFrom = resultFrom;
	}

	public String left;
	public String right;

	public Pairs(String left, String right, String resultFrom) {
		this.left = left;
		this.right = right;
		this.resultFrom = resultFrom;
	}

	public String getLeft() {
		return left;
	}

	public String getRight() {
		return right;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((resultFrom == null) ? 0 : resultFrom.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Pairs)) {
			return false;
		}

		Pairs user = (Pairs) obj;

		return user.left.equals(left) && user.resultFrom == resultFrom && user.right.equals(right)
				&& user.left.equals(right) && user.right.equals(left);
	}
	 @Override
	    public String toString()
	    {
	        return "Pair " + Integer.toHexString(hashCode()) + ": (" + left.toString() + ", " + right.toString()
	               + ")";
	    }

}