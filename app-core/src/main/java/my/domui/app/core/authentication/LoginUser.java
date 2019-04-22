package my.domui.app.core.authentication;

import my.domui.app.core.db.DbGroup;
import my.domui.app.core.db.DbGroupMember;
import my.domui.app.core.db.DbPermission;
import my.domui.app.core.db.DbUser;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import to.etc.domui.login.IUser2;
import to.etc.domui.state.UIContext;
import to.etc.webapp.query.QDataContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A context representing the logged-in user. Maintained in the
 * session context.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 */
@NonNullByDefault
public class LoginUser implements IUser2 {
	@NonNull
	private final String m_loginName;

	@NonNull
	final private DbUser m_user;

	/** Set when user has ADMIN right so all other checks are unneeded. */
	private boolean m_admin;

	private final Set<DbGroup> m_groupSet;

	private final Set<String> m_permissionNameSet;

	private final Set<DbPermission> m_permissionSet;

	public LoginUser(QDataContext dc, @NonNull final DbUser p) {
		m_user = p;
		m_loginName = p.getEmail();

		//-- Load all groups
		Set<String> groupNameSet = new HashSet<>();
		Set<String> permissionNameSet = new HashSet<>();
		Set<DbGroup> groupSet = new HashSet<>();
		Set<DbPermission> permSet = new HashSet<>();

		for(DbGroupMember membership : p.getGroupMemberList()) {
			DbGroup authGroup = membership.getGroup();
			groupSet.add(authGroup);
			groupNameSet.add(authGroup.getName());

			for(DbPermission permission : authGroup.getPermissionList()) {
				permSet.add(permission);
				permissionNameSet.add(permission.getName());
			}
		}

		m_permissionNameSet = Collections.unmodifiableSet(permissionNameSet);
		m_permissionSet = Collections.unmodifiableSet(permSet);
		m_groupSet = Collections.unmodifiableSet(groupSet);
	}

	@NonNull
	static public LoginUser create(@NonNull QDataContext dc, @NonNull DbUser whom) throws Exception {
		return new LoginUser(dc, whom);
	}

	@Nullable
	static public LoginUser	findCurrent() {
		return (LoginUser) UIContext.getCurrentUser();
	}

	@Nullable
	static public DbUser findCurrentUser() {
		LoginUser	lu = findCurrent();
		return lu == null ? null : lu.getUser();
	}

	@NonNull
	static public LoginUser	getCurrent() {
		return (LoginUser) UIContext.getLoggedInUser();
	}

	@NonNull
	static public DbUser getCurrentUser() {
		LoginUser lu = getCurrent();
		return lu.getUser();
	}

	@Override
	public String getLoginID() {
		return m_loginName;
	}

	@NonNull
	@Override
	public String getDisplayName() {
		DbUser user = m_user;
		return user == null ? "UNKNOWN" : user.getFullName();
	}

	public DbUser getUser() {
		return m_user;
	}

	/**
	 * Returns T if this user has this right assigned.
	 */
	@Override
	public boolean hasRight(@NonNull String r) {
		return m_permissionNameSet.contains(r) || m_permissionNameSet.contains(Rights.ADMIN);
	}

	@Override
	public <T> boolean hasRight(@NonNull String r, @Nullable T dataElement) {
		return true;
	}

	public Set<DbGroup> getGroupSet() {
		return m_groupSet;
	}

	public Set<String> getPermissionNameSet() {
		return m_permissionNameSet;
	}

	public Set<DbPermission> getPermissionSet() {
		return m_permissionSet;
	}

	@Override public boolean canImpersonate() {
		return hasRight(Rights.ADMIN);
	}
}
