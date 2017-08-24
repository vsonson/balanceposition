(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('network-member', {
            parent: 'entity',
            url: '/network-member?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'NetworkMembers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/network-member/network-members.html',
                    controller: 'NetworkMemberController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('network-member-detail', {
            parent: 'network-member',
            url: '/network-member/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'NetworkMember'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/network-member/network-member-detail.html',
                    controller: 'NetworkMemberDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'NetworkMember', function($stateParams, NetworkMember) {
                    return NetworkMember.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'network-member',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('network-member-detail.edit', {
            parent: 'network-member-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/network-member/network-member-dialog.html',
                    controller: 'NetworkMemberDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NetworkMember', function(NetworkMember) {
                            return NetworkMember.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('network-member.new', {
            parent: 'network-member',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/network-member/network-member-dialog.html',
                    controller: 'NetworkMemberDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                isDataShared: null,
                                sendAlerts: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('network-member', null, { reload: 'network-member' });
                }, function() {
                    $state.go('network-member');
                });
            }]
        })
        .state('network-member.edit', {
            parent: 'network-member',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/network-member/network-member-dialog.html',
                    controller: 'NetworkMemberDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NetworkMember', function(NetworkMember) {
                            return NetworkMember.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('network-member', null, { reload: 'network-member' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('network-member.delete', {
            parent: 'network-member',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/network-member/network-member-delete-dialog.html',
                    controller: 'NetworkMemberDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['NetworkMember', function(NetworkMember) {
                            return NetworkMember.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('network-member', null, { reload: 'network-member' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
