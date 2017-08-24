(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('incentive', {
            parent: 'entity',
            url: '/incentive?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Incentives'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/incentive/incentives.html',
                    controller: 'IncentiveController',
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
        .state('incentive-detail', {
            parent: 'incentive',
            url: '/incentive/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Incentive'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/incentive/incentive-detail.html',
                    controller: 'IncentiveDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Incentive', function($stateParams, Incentive) {
                    return Incentive.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'incentive',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('incentive-detail.edit', {
            parent: 'incentive-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/incentive/incentive-dialog.html',
                    controller: 'IncentiveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Incentive', function(Incentive) {
                            return Incentive.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('incentive.new', {
            parent: 'incentive',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/incentive/incentive-dialog.html',
                    controller: 'IncentiveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                desc: null,
                                pointvalue: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('incentive', null, { reload: 'incentive' });
                }, function() {
                    $state.go('incentive');
                });
            }]
        })
        .state('incentive.edit', {
            parent: 'incentive',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/incentive/incentive-dialog.html',
                    controller: 'IncentiveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Incentive', function(Incentive) {
                            return Incentive.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('incentive', null, { reload: 'incentive' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('incentive.delete', {
            parent: 'incentive',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/incentive/incentive-delete-dialog.html',
                    controller: 'IncentiveDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Incentive', function(Incentive) {
                            return Incentive.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('incentive', null, { reload: 'incentive' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
