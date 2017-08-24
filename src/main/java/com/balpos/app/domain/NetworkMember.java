package com.balpos.app.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A NetworkMember.
 */
@Entity
@Table(name = "network_member")
public class NetworkMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_data_shared")
    private Boolean isDataShared;

    @Column(name = "send_alerts")
    private Boolean sendAlerts;

    @ManyToOne
    private UserInfo networkMemberUser;

    @ManyToOne
    private UserInfo networkOwner;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsDataShared() {
        return isDataShared;
    }

    public NetworkMember isDataShared(Boolean isDataShared) {
        this.isDataShared = isDataShared;
        return this;
    }

    public void setIsDataShared(Boolean isDataShared) {
        this.isDataShared = isDataShared;
    }

    public Boolean isSendAlerts() {
        return sendAlerts;
    }

    public NetworkMember sendAlerts(Boolean sendAlerts) {
        this.sendAlerts = sendAlerts;
        return this;
    }

    public void setSendAlerts(Boolean sendAlerts) {
        this.sendAlerts = sendAlerts;
    }

    public UserInfo getNetworkMemberUser() {
        return networkMemberUser;
    }

    public NetworkMember networkMemberUser(UserInfo userInfo) {
        this.networkMemberUser = userInfo;
        return this;
    }

    public void setNetworkMemberUser(UserInfo userInfo) {
        this.networkMemberUser = userInfo;
    }

    public UserInfo getNetworkOwner() {
        return networkOwner;
    }

    public NetworkMember networkOwner(UserInfo userInfo) {
        this.networkOwner = userInfo;
        return this;
    }

    public void setNetworkOwner(UserInfo userInfo) {
        this.networkOwner = userInfo;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NetworkMember networkMember = (NetworkMember) o;
        if (networkMember.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), networkMember.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NetworkMember{" +
            "id=" + getId() +
            ", isDataShared='" + isIsDataShared() + "'" +
            ", sendAlerts='" + isSendAlerts() + "'" +
            "}";
    }
}
