(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('path-action', {
            parent: 'entity',
            url: '/path-action?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PathActions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/path-action/path-actions.html',
                    controller: 'PathActionController',
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
        .state('path-action-detail', {
            parent: 'path-action',
            url: '/path-action/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PathAction'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/path-action/path-action-detail.html',
                    controller: 'PathActionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PathAction', function($stateParams, PathAction) {
                    return PathAction.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'path-action',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('path-action-detail.edit', {
            parent: 'path-action-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/path-action/path-action-dialog.html',
                    controller: 'PathActionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PathAction', function(PathAction) {
                            return PathAction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('path-action.new', {
            parent: 'path-action',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/path-action/path-action-dialog.html',
                    controller: 'PathActionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                actionUrl: null,
                                actionType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('path-action', null, { reload: 'path-action' });
                }, function() {
                    $state.go('path-action');
                });
            }]
        })
        .state('path-action.edit', {
            parent: 'path-action',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/path-action/path-action-dialog.html',
                    controller: 'PathActionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PathAction', function(PathAction) {
                            return PathAction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('path-action', null, { reload: 'path-action' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('path-action.delete', {
            parent: 'path-action',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/path-action/path-action-delete-dialog.html',
                    controller: 'PathActionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PathAction', function(PathAction) {
                            return PathAction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('path-action', null, { reload: 'path-action' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
