package com.marketplace.Auth.domain;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public void saveRole(Role role) {
        Objects.requireNonNull(role);
        roleRepository.save(role);
    }

    @Transactional
    public Role getOrCreateRoleAccount(Role.RoleEnum roleName, Set<Permission> permissionSet) {
        Role role = roleRepository.findByRoleName(roleName).orElse(null);
        if (role == null) {
            Role newRole = new Role(roleName, permissionSet);
            saveRole(newRole);
            return newRole;
        }

        return role;
    }

//    public Role getOrCreateRoleAccount(Role.RoleEnum roleName) {
//        Transaction tx = null;
//        Role role = null;
//        System.out.println("get or create role account run");
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            System.out.println("Come in");
//            TypedQuery<Role> query = session.createQuery("FROM Role r WHERE r.roleName =:role_name", Role.class);
//            query.setParameter("role_name", roleName);
//            List<Role> roles = query.getResultList();
//            if (roles.isEmpty()) {
//                tx = session.beginTransaction();
//                Role newRole = new Role(roleName);
//                System.out.println("newRole = " + newRole);
//                System.out.println("newRole name = " + newRole.getRoleName());
//                session.persist(newRole);
//                role = newRole;
//                tx.commit();
//                System.out.println("new role is = " + role);
////                return role;
//            }
//            role = roles.getFirst();
//            System.out.println("old role is = " + role);
//
//        } catch (Exception e) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            e.printStackTrace();
//        }
//        return role;
//    }

}
