package kernel.enums;

public enum  FunctionType {

    INJECTIVE(SubsetType.CONTAINED, SubsetType.CONTAINS),
    SURJECTIVE(SubsetType.CONTAINS, SubsetType.CONTAINED),
    BIJECTIVE(SubsetType.BIJECTIVE, SubsetType.BIJECTIVE),
    NONE(SubsetType.NONE, SubsetType.NONE);

    private final SubsetType domainType;
    private final SubsetType codomainType;

    FunctionType(
            final SubsetType domainType,
            final SubsetType codomainType) {
        this.domainType = domainType;
        this.codomainType = codomainType;
    }

    public SubsetType getDomainType() {
        return domainType;
    }

    public SubsetType getCodomainType() {
        return codomainType;
    }
}
