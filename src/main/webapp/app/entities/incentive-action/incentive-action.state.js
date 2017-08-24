(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('incentive-action', {
            parent: 'entity',
            url: '/incentive-action?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'IncentiveActions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/incentive-action/incentive-actions.html',
                    controller: 'IncentiveActionController',
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
        .state('incentive-action-detail', {
            parent: 'incentive-action',
            url: '/incentive-action/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'IncentiveAction'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/incentive-action/incentive-action-detail.html',
                    controller: 'IncentiveActionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'IncentiveAction', function($stateParams, IncentiveAction) {
                    return IncentiveAction.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'incentive-action',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('incentive-action-detail.edit', {
            parent: 'incentive-action-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/incentive-action/incentive-action-dialog.html',
                    controller: 'IncentiveActionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['IncentiveAction', function(IncentiveAction) {
                            return IncentiveAction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('incentive-action.new', {
            parent: 'incentive-action',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/incentive-action/incentive-action-dialog.html',
                    controller: 'IncentiveActionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('incentive-action', null, { reload: 'incentive-action' });
                }, function() {
                    $state.go('incentive-action');
                });
            }]
        })
        .state('incentive-action.edit', {
            parent: 'incentive-action',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/incentive-action/incentive-action-dialog.html',
                    controller: 'IncentiveActionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['IncentiveAction', function(IncentiveAction) {
                            return IncentiveAction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('incentive-action', null, { reload: 'incentive-action' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('incentive-action.delete', {
            parent: 'incentive-action',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/incentive-action/incentive-action-delete-dialog.html',
                    controller: 'IncentiveActionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['IncentiveAction', function(IncentiveAction) {
                            return IncentiveAction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('incentive-action', null, { reload: 'incentive-action' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
